#clasa abstracta pentru starile automatului
class State:
    def __init__(self, state_machine):
        self.state_machine = state_machine


class Observable:
    def __init__(self):
        self.observers = []

    def attach(self, observer): #adauga "observatori" in lista
        self.observers.append(observer)

    def detach(self, observer):
        self.observers.remove(observer)

    def notifyAll(self, *args, **kwargs): #functie pentru a oferi update "observatorilor"
        for observer in self.observers:
            observer.update(*args, **kwargs)


class DisplayObserver: #ce vede clientul
    def update(self, money):
        print(f"[Display] Suma introdusă: {money} bani")


class ChoiceObserver: #notifica automatul central sa verifice optiunea selectata
    def __init__(self, vending_machine):
        self.vending_machine = vending_machine

    def update(self):
        self.vending_machine.proceed_to_checkout()


class WaitingForClient(State): #starea initiala a automatului TakeMoneySTM
    def client_arrived(self):
        print("[Simulare]Client a sosit.")
        self.state_machine.current_state = self.state_machine.insert_money_state #trecerea din starea initiala in starea InsertMoney


class InsertMoney(State):
    def insert_10bani(self):
        self.state_machine.add_money(10)

    def insert_50bani(self):
        self.state_machine.add_money(50)

    def insert_1leu(self):
        self.state_machine.add_money(100)

    def insert_5lei(self):
        self.state_machine.add_money(500)

    def insert_10lei(self):
        self.state_machine.add_money(1000)


class TakeMoneySTM(Observable): #automat pentru introducerea banilor
    def __init__(self):
        super().__init__()
        self.wait_state = WaitingForClient(self)
        self.insert_money_state = InsertMoney(self)
        self.current_state = self.wait_state
        self.money = 0

    def add_money(self, value):
        self.money += value
        print(f"[Simulare] S-au adăugat {value} bani. Total: {self.money} bani")
        self.notifyAll(self.money)

    def update_amount_of_money(self, value):
        self.money = value
        print(f"[Simulare] Suma resetată la {value} bani.")
        self.notifyAll(self.money)



class SelectProduct(State):
    def choose(self):
        pass


class CocaCola(SelectProduct):
    price = 300

    def choose(self):
        print("[Display] CocaCola selectată.")
        self.state_machine.current_state = self
        self.state_machine.notifyAll() # se anunta ChoiceObserver ca produsul x a fost ales


class Pepsi(SelectProduct):
    price = 250

    def choose(self):
        print("[Display] Pepsi selectat.")
        self.state_machine.current_state = self
        self.state_machine.notifyAll()


class Sprite(SelectProduct):
    price = 200

    def choose(self):
        print("[Display] Sprite selectat.")
        self.state_machine.current_state = self
        self.state_machine.notifyAll()


class SelectProductSTM(Observable):
    def __init__(self):
        super().__init__()
        self.select_product_state = SelectProduct(self)
        self.coca_cola_state = CocaCola(self)
        self.pepsi_state = Pepsi(self)
        self.sprite_state = Sprite(self)
        self.current_state = self.select_product_state

    def choose_another_product(self):
        self.current_state = self.select_product_state
        print("[Display] Selectați un alt produs.")



class VendingMachineSTM:
    def __init__(self):
        self.take_money_stm = TakeMoneySTM()
        self.select_product_stm = SelectProductSTM()

        #se conecteaza DisplayObserver cu TakeMoney pentru a actualiza clientul cu suma de bani introdusa
        self.display_observer = DisplayObserver()
        self.take_money_stm.attach(self.display_observer)
        #aici select_product se conecteaza cu ChoiceObserver pentru a declansa startul tranzactiei
        self.choice_observer = ChoiceObserver(self)
        self.select_product_stm.attach(self.choice_observer)

    def proceed_to_checkout(self):
        selected_state = self.select_product_stm.current_state
        if isinstance(selected_state, CocaCola):
            price = CocaCola.price
            product = "CocaCola"
        elif isinstance(selected_state, Pepsi):
            price = Pepsi.price
            product = "Pepsi"
        elif isinstance(selected_state, Sprite):
            price = Sprite.price
            product = "Sprite"
        else:
            print("[Display] Niciun produs selectat.")
            return

        print(f"[Simulare] Se verifică banii pentru {product} ({price} bani).")

        if self.take_money_stm.money >= price:
            print(f"[Display] Tranzacție reușită. {product} livrat.")
            rest = self.take_money_stm.money - price
            if rest > 0:
                print(f"[Display] Rest: {rest} bani.")
            else:
                print("[Simulare] Fără rest.")
            self.take_money_stm.update_amount_of_money(0)
            self.select_product_stm.choose_another_product()
        else:
            print(f"[Display] Fonduri insuficiente. Mai adăugați {price - self.take_money_stm.money} bani.")



def main():
    vm = VendingMachineSTM()

    print("\n[Simulare]\nClient introduce bani")
    vm.take_money_stm.current_state.client_arrived()
    vm.take_money_stm.current_state.insert_50bani()
    vm.take_money_stm.current_state.insert_1leu()

    print("\nClient selectează produs")
    vm.select_product_stm.coca_cola_state.choose()

    print("\nClient mai introduce bani")
    vm.take_money_stm.current_state.insert_1leu()

    print("\nClient selectează alt produs")
    vm.select_product_stm.sprite_state.choose()


if __name__ == "__main__":
    main()
