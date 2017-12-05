import threading
from tkinter import *


class GuiThread:
    counter = 0
    label = None

    def increment(self):
        print("hello")

    def throw(self):
        gui = threading.Thread()
        root = Tk()
        label = Label(root, text="whats up dock")
        label.pack()
        root.mainloop()
