import subprocess
import re
import os
from abc import ABC, abstractmethod


class FileHandler(ABC):
    def __init__(self):
        self._next = None

    def set_next(self, handler):
        self._next = handler
        return handler

    @abstractmethod
    def identify(self, content, filepath):
        if self._next:
            return self._next.identify(content,filepath)
        raise ValueError ("Unknown file type")


class KotlinHandler(FileHandler):
  def identify(self, content, filepath):
      first = content.splitlines()[0] if content else ''
      if((first.startswith("#!") and "kotlin" in first) or 'fun main' in content):
          return KotlinCommand(filepath)
      return super().identify(content,filepath)


class PythonHandler(FileHandler):
    def identify(self, content, filepath):
        first = content.splitlines()[0] if content else ''
        if ((first.startswith("#!") and "python" in first) or 'def' in content or 'import'in content):
            return PythonCommand(filepath)
        return super().identify(content, filepath)


class BashHandler(FileHandler):
    def identify(self, content, filepath):
        first = content.splitlines()[0] if content else ''
        if ((first.startswith("#!") and ("bash" in first or "sh" in first)) or content.strip().startswith("echo ")):
            return BashCommand(filepath)
        return super().identify(content, filepath)


class JavaHandler(FileHandler):
    def identify(self, content, filepath):
        if 'public class ' in content and 'static void main' in content:
            for line in content.splitlines():
                line = line.strip()
                if line.startswith('public class '):
                    class_name = line.split()[2]
                    return JavaCommand(filepath , class_name)
        return super().identify(content, filepath)


class Command(ABC):
    def __init__(self,filepath):
        self.filepath = filepath
    @abstractmethod
    def execute(self):
        pass


class KotlinCommand(Command):
    def execute(self):
        run = subprocess.run(['kotlinc', self.filepath, '-include-runtime', '-d', 'temp.jar'],
                             capture_output=True, text=True)
        if run.returncode != 0:
            return run.stderr

        run = subprocess.run(['java', '-jar', 'temp.jar'],
                             capture_output=True, text=True)
        return run.stdout or run.stderr

class PythonCommand(Command):
    def execute(self):
        run = subprocess.run(['python', self.filepath],
                             capture_output=True, text=True)
        return run.stdout or run.stderr


class BashCommand(Command):
    def execute(self):
        os.chmod(self.filepath, 0o755)
        run = subprocess.run([self.filepath],
                             capture_output=True, text=True, shell=True)
        return run.stdout or run.stderr


class JavaCommand(Command):
    def __init__(self, filepath, class_name):
        super().__init__(filepath)
        self.class_name = class_name

    def execute(self):
        try:
            
            with open(self.filepath, 'r') as f:
                content = f.read()

            temp_filename = self.class_name + '.java'

            
            with open(temp_filename, 'w') as f:
                f.write(content)

            
            compile = subprocess.run(['javac', temp_filename],
                                     capture_output=True, text=True)
            if compile.returncode != 0:
                return f"Eroare la compilare Java:\n{compile.stderr}"

            run = subprocess.run(['java', self.class_name],
                                 capture_output=True, text=True)
            return run.stdout or run.stderr

        finally:
            if os.path.exists(temp_filename):
                os.remove(temp_filename)
            class_file = self.class_name + '.class'
            if os.path.exists(class_file):
                os.remove(class_file)



def main():
    filepath = input("Introduceți calea completă către fișierul fără extensie: ").strip()

    if not os.path.isfile(filepath):
        print(f"Fișierul '{filepath}' nu a fost găsit!")
        return

    try:
        with open(filepath, 'r') as f:
            content = f.read()
    except Exception as e:
        print(f"Eroare la citirea fișierului: {e}")
        return

    handler = KotlinHandler()
    handler.set_next(PythonHandler()) \
           .set_next(BashHandler()) \
           .set_next(JavaHandler())

    try:
        command = handler.identify(content, filepath)
    except ValueError as e:
        print(e)
        return

    print("\n=== Rezultat rulare ===")
    output = command.execute()
    print(output)

if __name__ == "__main__":
    main()
