<<<<<<< HEAD
import tkinter as tk
from tkinter import messagebox, Menu
import random

class Graphic:
    def __init__(self, root):
        self.root = root
        self.root.title("Hangman Game")
        self.canvas = tk.Canvas(root, width=400, height=300)
        self.canvas.pack()
        self.game_logic = GameLogic(Dictionar().get_word())

        self.init_ui()

    def init_ui(self):
        menu_bar = Menu(self.root)
        game_menu = Menu(menu_bar, tearoff=0)
        game_menu.add_command(label="New Game", command=self.new_game)
        menu_bar.add_cascade(label="Game", menu=game_menu)
        self.root.config(menu=menu_bar)

        self.draw_hangman()
        self.draw_word()

        self.root.bind("<Key>", self.on_key_press)

    def new_game(self):
        self.game_logic.new_game(Dictionar().get_word())
        self.draw_hangman()
        self.draw_word()

    def on_key_press(self, event):
        letter = event.char.lower()
        if letter.isalpha() and len(letter) == 1:
            self.game_logic.guess_letter(letter)
            self.draw_hangman()
            self.draw_word()
            if self.game_logic.is_game_over():
                if self.game_logic.is_won():
                    self.show_message("Congratulations! You won!")
                else:
                    self.show_message(f"Game Over! The word was: {self.game_logic.word}")
                self.new_game()

    def draw_hangman(self):
        self.canvas.delete("all")
        # Draw base
        self.canvas.create_line(50, 250, 150, 250)
        self.canvas.create_line(100, 50, 100, 250)
        self.canvas.create_line(100, 50, 200, 50)
        self.canvas.create_line(200, 50, 200, 75)

        wrong_guesses = self.game_logic.wrong_guesses
        if wrong_guesses > 0:
            self.canvas.create_oval(175, 75, 225, 125)  # Head
        if wrong_guesses > 1:
            self.canvas.create_line(200, 125, 200, 175)  # Body
        if wrong_guesses > 2:
            self.canvas.create_line(200, 135, 150, 100)  # Left arm
        if wrong_guesses > 3:
            self.canvas.create_line(200, 135, 250, 100)  # Right arm
        if wrong_guesses > 4:
            self.canvas.create_line(200, 175, 150, 250)  # Left leg
        if wrong_guesses > 5:
            self.canvas.create_line(200, 175, 250, 250)  # Right leg

    def draw_word(self):
        display_word = self.game_logic.get_display_word()
        self.canvas.create_text(200, 280, text=display_word, font=("Helvetica", 24))

    def show_message(self, message):
        messagebox.showinfo("Hangman Game", message)


class GameLogic:
    def __init__(self, word):
        self.word = word
        self.guessed_letters = set()
        self.wrong_guesses = 0

    def new_game(self, new_word):
        self.word = new_word
        self.guessed_letters.clear()
        self.wrong_guesses = 0

    def guess_letter(self, letter):
        if letter in self.word and letter not in self.guessed_letters:
            self.guessed_letters.add(letter)
        else:
            self.wrong_guesses += 1

    def is_game_over(self):
        return self.wrong_guesses >= 6 or all(c in self.guessed_letters for c in self.word)

    def is_won(self):
        return all(c in self.guessed_letters for c in self.word)

    def get_display_word(self):
        return ' '.join(c if c in self.guessed_letters else '_' for c in self.word)


class Dictionar:
    def __init__(self):
        self.words = ["apple", "banana", "grape", "orange", "lemon", "melon", "berry", "kiwi", "peach", "plum"]

    def get_word(self):
        return random.choice(self.words)


if __name__ == "__main__":
    root = tk.Tk()
    app = Graphic(root)
    root.mainloop()
=======
import re

def read_file(filename):
    with open(filename,'r') as file:
        return file.read()
def process_text(text):
    text=re.sub(r'[.,!?;:\'\"()\[\]{}]','',text);
    text=re.sub(r'\s+', ' ',text).strip()
    text=text.lower()
    return text
def main():
    filename='input'
    text=read_file(filename)

    print("Textul initial: \n",text)
    processd_text=process_text(text)
    print("\nTextul procesat:\n",processd_text)

if __name__=="__main__":
    main()
>>>>>>> 608442e (First commit)
