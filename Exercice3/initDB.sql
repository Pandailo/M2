create table Compte(id NUMBER(6) NOT NULL PRIMARY KEY,balance NUMBER(12),Client_id NUMBER(6));
create table Operations(
	id NUMBER(12) NOT NULL PRIMARY KEY,type VARCHAR(25),montant NUMBER(12),accountId NUMBER(6),createdDate TIMESTAMP, 
	CONSTRAINT fk_accountid FOREIGN KEY (accountId) REFERENCES Accounts(id),
	CONSTRAINT c_type CHECK (type IN ('retrait','depot'))
);
create table Client(id NUMBER(6) NOT NULL PRIMARY KEY,name VARCHAR(250));

create or replace trigger incrAccountId BEFORE INSERT on Compte FOR EACH ROW
Declare
	maxId NUMBER(6);
BEGIN
	SELECT nvl(max(id),0) INTO maxId FROM Accounts;
	maxId := maxId+1 ;
	:new.id := maxId;
END;
/
create or replace trigger incrOperationId BEFORE INSERT on Operations FOR EACH ROW
Declare
	maxId NUMBER(6);
BEGIN
	SELECT nvl(max(id),0) INTO maxId FROM Operations;
	maxId := maxId+1 ;
	:new.id := maxId;
END;
/
create or replace trigger incrClientId BEFORE INSERT on Client FOR EACH ROW
Declare
	maxId NUMBER(6);
BEGIN
	SELECT nvl(max(id),0) INTO maxId FROM Compte;
	maxId := maxId+1 ;
	:new.id := maxId;
END;
/
create or replace trigger triggerOpe AFTER INSERT on Operations FOR EACH ROW
Declare
	oldBalance NUMBER(12);
	newBalance NUMBER(12);
	operationAmount NUMBER(12);
	accountId NUMBER(7);
BEGIN
	IF(:new.type like 'retrait%')
	THEN
		SELECT solde INTO oldBalance FROM Compte WHERE id = :new.accountId;
		operationAmount := :new.montant;
		newBalance := oldBalance - operationAmount;
		accountId := :new.accountId;
		UPDATE Compte SET solde = newBalance WHERE id = accountId;
	ELSE
		SELECT nvl(solde,0) INTO oldBalance FROM Compte WHERE id = :new.accountId;
		operationAmount := :new.montant;
		newBalance := oldBalance + operationAmount;
		accountId := :new.accountId;
		UPDATE Compte SET solde = newBalance WHERE id = accountId;
	END IF;
END;
/

CREATE OR REPLACE PROCEDURE proc_init_Compte(numberOfAccount IN NUMBER)
IS
	balance NUMBER(30);
BEGIN	
	for i IN 0..numberOfAccount LOOP
		balance := DBMS_RANDOM.VALUE(0, 1000000);
		INSERT INTO Compte VALUES(0,balance);
	end LOOP;
END;
/

CREATE OR REPLACE PROCEDURE proc_init_operations(numberOfOperation IN NUMBER)
IS
	operationAmount NUMBER(30);
	accountId NUMBER(6);
	typeOpe NUMBER(1);
	identifier NUMBER(30);
BEGIN
	for i IN 0..numberOfOperation LOOP
		operationAmount := DBMS_RANDOM.VALUE(0, 100000); 
		accountId := DBMS_RANDOM.VALUE(0, 200000); 
		select round(DBMS_RANDOM.VALUE (0, 1)) into typeOpe from dual;
		IF(typeOpe=0)
		THEN
			
			INSERT INTO Operations VALUES (identifier,'retrait',operationAmount,accountId,sysdate);
		ELSE
		
			INSERT INTO Operations VALUES (identifier,'depot',operationAmount,accountId,sysdate);
		END IF;
	end LOOP;
END;
/ 

CREATE OR REPLACE PROCEDURE operation(accountId IN NUMBER,amount IN NUMBER,typeOpe VARCHAR)
IS
BEGIN
	IF(typeOpe='retrait')
		THEN
			
			INSERT INTO Operations VALUES (0,'retrait',amount,accountId,sysdate);
		ELSE
		
			INSERT INTO Operations VALUES (0,'depot',amount,accountId,sysdate);
		END IF;
END;
/
CREATE OR REPLACE FUNCTION createAccount(newSolde IN Compte.solde%TYPE) RETURN NUMBER
AS
v_id Compte.solde%TYPE;
BEGIN
	INSERT INTO Compte(solde) values(NewSolde) returning id into v_id;
	RETURN v_id;
END;
/
