import sys
import os
import queue
import subprocess
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QFileDialog, QVBoxLayout, QTextEdit, QLabel, QLineEdit

class HTMLConverter(QWidget):
    OUTPUT_FILE = "output.html"
    VALIDATED_FILE = "validated_output.html"

    def __init__(self):
        super().__init__()
        self.init_ui()
        self.msg_queue = queue.Queue()

    def init_ui(self):
        self.setWindowTitle("Text to HTML Converter")
        self.setGeometry(100, 100, 500, 400)

        self.label = QLabel("Introduceți calea fișierului:", self)
        self.textbox = QLineEdit(self)
        self.browse_button = QPushButton("Browse", self)
        self.convert_button = QPushButton("Convert to HTML", self)
        self.result_text_edit = QTextEdit(self)

        layout = QVBoxLayout()
        layout.addWidget(self.label)
        layout.addWidget(self.textbox)
        layout.addWidget(self.browse_button)
        layout.addWidget(self.convert_button)
        layout.addWidget(self.result_text_edit)
        self.setLayout(layout)

        self.browse_button.clicked.connect(self.browse_file)
        self.convert_button.clicked.connect(self.convert_to_html)

    def browse_file(self):
        file_path, _ = QFileDialog.getOpenFileName(self, "Select File", "", "Text Files (*.txt)")
        if file_path:
            self.textbox.setText(file_path)

    def convert_to_html(self):
        file_path = self.textbox.text()
        if not os.path.exists(file_path):
            self.result_text_edit.append("Error: File not found!")
            return

        try:
            with open(file_path, "r", encoding="utf-8") as file:
                lines = file.readlines()

            html_content = "<html>\n<body>\n"
            for i, line in enumerate(lines):
                line = line.strip()
                if i == 0:  # Primul rând ca titlu
                    html_content += f"<h1>{line}</h1>\n"
                elif line:
                    html_content += f"<p>{line}</p>\n"
            html_content += "</body>\n</html>"

            with open(self.OUTPUT_FILE, "w", encoding="utf-8") as html_file:
                html_file.write(html_content)

            self.msg_queue.put(html_content)
            self.result_text_edit.append("HTML conversion successful! Sending to C validator...")

            self.send_to_c_application()

        except Exception as e:
            self.result_text_edit.append(f"Error: {e}")

    def send_to_c_application(self):
        exe_path = os.path.abspath("html_validator.exe")
        try:
            subprocess.run([exe_path], check=True, shell=True)
            self.result_text_edit.append(f"Validation completed! See {self.VALIDATED_FILE}")
        except Exception as e:
            self.result_text_edit.append(f"Error running C application: {e}")

if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = HTMLConverter()
    window.show()
    sys.exit(app.exec_())
