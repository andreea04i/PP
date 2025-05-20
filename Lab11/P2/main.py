import subprocess

def executa_pipeline(comanda):
    comenzi = [cmd.strip() for cmd in comanda.split('|')]
    procese = []

    for i, cmd in enumerate(comenzi):
        if i == 0:
            p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE)
        else:
            p = subprocess.Popen(cmd, shell=True, stdin=procese[-1].stdout, stdout=subprocess.PIPE)
        procese.append(p)

    output, _ = procese[-1].communicate()
    print(output.decode())

if __name__ == '__main__':
    comanda = "dir /b *.txt | find /c /v \"\""
    executa_pipeline(comanda)