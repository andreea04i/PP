import os
import struct
from abc import ABC, abstractmethod


class GenericFile(ABC):
    @abstractmethod
    def get_path(self): #returneaza calea fisierului
        pass

    @abstractmethod
    def get_freq(self): #returneaza frecventele calculate
        pass
    @abstractmethod
    def compute_frequencies(self, content): #calculeaza frecventele cracterelor din fisier
       pass


class TextASCII(GenericFile):
    def __init__(self, path, content): #constructor pentru initializare
        self.path_absolut = path
        self.frecvente = self.compute_frequencies(content)

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

    def compute_frequencies(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content: #parcurge fiecare byte din continutul fisierului
            freq[byte] += 1
        return freq

class TextUNICODE(GenericFile):
    def __init__(self, path, content):
        self.path_absolut = path
        self.frecvente = self.compute_frequencies(content)

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

    def compute_frequencies(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content:
            freq[byte] += 1
        return freq


class Binary(GenericFile):
    def __init__(self, path, content):
        self.path_absolut = path
        self.frecvente = self.compute_frequencies(content)

    def get_path(self):
        return self.path_absolut

    def get_freq(self):
        return self.frecvente

    def compute_frequencies(self, content):
        freq = {i: 0 for i in range(256)}
        for byte in content:
            freq[byte] += 1
        return freq


class XMLFile(TextASCII):
    def __init__(self, path, content):
        super().__init__(path, content) #apeleaza constructorul clasei de baza
        self.first_tag = self.get_first_tag(content)

    def get_first_tag(self, content): #functie pentru a cauta primul tag XML
        try:
            text = content.decode('ascii', errors='ignore')
            start = text.find('<')
            end = text.find('>', start)
            if start != -1 and end != -1 and '</' in text:
                return text[start:end + 1] #returneaza textul cuprins intre <>
        except:
            return None
        return None


class BMP(Binary):
    def __init__(self, path, content):
        super().__init__(path, content)
        self.width, self.height, self.bpp = self.extract_info(content)

    def extract_info(self, content):
        if len(content) >= 30 and content[:2] == b'BM': # verificam daca primii 2 octeti sunt BM (de la BMP)
            width = struct.unpack('<I', content[18:22])[0] #latimea imaginii
            height = struct.unpack('<I', content[22:26])[0] #inaltimea
            bpp = struct.unpack('<H', content[28:30])[0] #biti per pixel
            return width, height, bpp
        return None, None, None

    def show_info(self):
        return f'{self.get_path()} - Width: {self.width}, Height: {self.height}, BPP: {self.bpp}'


def identify_file_type(path, content):
    if len(content) >= 30 and content[:2] == b'BM': #verificam daca este un fisier de tip BMP (primii doi octeti din extensie sunt BM)
        return BMP(path, content)

    text_ascii = TextASCII(path, content) #creeaza un obiect de tip TextASCII
    freq = text_ascii.get_freq() #extrage frecventele caracterelor

    total_chars = sum(freq.values()) #calculeaza nr. total de caractere

    if total_chars == 0: #caz in care fisierul este gol
        return None

    ascii_freq = sum(freq[i] for i in range(9, 128)) / total_chars  # calculeaza frecventa pentru caractere ASCII
    unicode_freq = freq[0] / total_chars  # calculeaza frecvența pentru caracterul 0

    # Verificăm tipul de fișier pe baza frecvenței caracterelor
    if ascii_freq > 0.9 and unicode_freq < 0.01:  # Fișier ASCII
        text = content.decode('ascii', errors='ignore')
        if '<' in text and '>' in text and '</' in text:  # verificăm dacă conține tag-uri XML
            return XMLFile(path, content)
        return TextASCII(path, content)

    elif unicode_freq > 0.4:  # Fișier UNICODE
        return TextUNICODE(path, content)

    return Binary(path, content)  # Fișier binar

def scan_directory(directory):
    results = {
        'xml': [],
        'unicode': [],
        'bmp': []
    }
    for root, _, files in os.walk(directory): #parcurge fisierele din director
        for file in files:
            file_path = os.path.join(root, file) # construieste calea fiserelui
            if os.path.isfile(file_path):
                with open(file_path, 'rb') as f:
                    content = f.read() #citeste continutul fisierului
                    identified_file = identify_file_type(file_path, content) #identifica tipul fisierului
                    if isinstance(identified_file, XMLFile): #isintance returneaza True /False daca identified_file este sau nu fisier XML
                        results['xml'].append(identified_file.get_path())
                    elif isinstance(identified_file, TextUNICODE):
                        results['unicode'].append(identified_file.get_path())
                    elif isinstance(identified_file, BMP):
                        results['bmp'].append(identified_file.show_info())
    return results


directory_to_scan = "C:\\Users\\HP\\Desktop\\PP\\Lab6\\fisier_cu_texte"
scan_results = scan_directory(directory_to_scan)

print("XML ASCII Files:", scan_results['xml'])
print("UNICODE Files:", scan_results['unicode'])
print("BMP Files:", scan_results['bmp'])
