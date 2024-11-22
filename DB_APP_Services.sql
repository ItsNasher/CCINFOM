CREATE TABLE Product (
Product_ID INT PRIMARY KEY,
    Product_Name VARCHAR(100) NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    Description TEXT
);

CREATE TABLE Inventory (
	Inventory_ID INT PRIMARY KEY,
    Product_ID INT NOT NULL,
    Stock_Quantity INT NOT NULL,
    Supply_Date DATE NOT NULL,
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)
);

CREATE TABLE Sales_Transaction (
	Transaction_ID INT PRIMARY KEY,
    Product_ID INT NOT NULL,
    Quantity_Sold INT NOT NULL,
    Sale_Date DATE NOT NULL,
    Total_Price DECIMAL (10,2) NOT NULL,
    FOREIGN KEY (Product_ID) REFERENCES Product(Product_ID)
);

INSERT INTO Product (Product_ID, Product_Name, Price, Description)
VALUES (1, "Dog Food", 300.00, "A nutritious dog food made with real chicken, wholesome grains, and essential vitamins to support your pet’s health and vitality."),
	   (2, "Cat Food", 200.79, "Balanced cat food with real fish and essential nutrients for a healthy coat and strong immune system."),
	   (3, "Grooming Shampoo", 150.69, "Gentle shampoo that cleans, deodorizes, and nourishes your pet’s skin and coat."),
       (4, "Collar & Leash", 200.29, "Durable and adjustable set for secure walks and stylish comfort."),
       (5, "Pet Bed", 500.00, "Cozy and soft bed designed for optimal comfort and restful sleep.");
       
INSERT INTO Inventory (Inventory_ID, Product_ID, Stock_Quantity, Supply_Date)
VALUES (1, 1, 20, '2024-11-01'),
       (2, 2, 30, '2024-11-05'),
       (3, 3, 10, '2024-11-06'),
       (4, 4, 20, '2024-11-06'),
       (5, 5, 8, '2024-11-10');
       
INSERT INTO Sales_Transaction (Transaction_ID, Product_ID, Quantity_Sold, Sale_Date, Total_Price)
VALUES (1, 1, 1, '2024-11-06', 300.00), 
       (2, 2, 1, '2024-11-06', 200.79), 
       (3, 3, 1, '2024-11-07', 150.69), 
       (4, 4, 1, '2024-11-08', 200.29), 
       (5, 5, 3, '2024-11-09', 1500.00); 

CREATE TABLE Services (
	Service_ID INT PRIMARY KEY,
    Service_Name VARCHAR(50) NOT NULL,
    Start_Date DATE NOT NULL,
    End_Date DATE NOT NULL,
    Price DECIMAL (10, 2)
);

CREATE TABLE Owner (
    Owner_ID INT AUTO_INCREMENT PRIMARY KEY,
    First_Name VARCHAR(20) NOT NULL,
    Last_Name VARCHAR(20) NOT NULL,
    Contact_Info VARCHAR(15) NOT NULL, 
    City VARCHAR(30) ,
    Street VARCHAR(30) NOT NULL,
    Postal_Code INT NOT NULL
) AUTO_INCREMENT = 1000;

CREATE TABLE Pet (
    Pet_ID INT AUTO_INCREMENT PRIMARY KEY,
    Pet_Name VARCHAR(30) NOT NULL,
    Breed VARCHAR(30) NOT NULL,
    Age INT NOT NULL,
    HealthInformation VARCHAR(50),
    Owner_ID INT NOT NULL,
    FOREIGN KEY (Owner_ID) REFERENCES Owner(Owner_ID) ON DELETE CASCADE
) AUTO_INCREMENT = 10000;


CREATE TABLE Services_Transaction (
    Transaction_ID INT PRIMARY KEY,
    Service_ID INT NOT NULL,
    Owner_ID INT NOT NULL,
    Transaction_Date DATE NOT NULL,
    Quantity INT DEFAULT 1, -- Used mostly for the boarding (multiple days)
    Total_Amount DECIMAL(10, 2),
    FOREIGN KEY (Service_ID) REFERENCES Services(Service_ID),
    FOREIGN KEY (Owner_ID) REFERENCES Owner(Owner_ID)
);

INSERT INTO Services (Service_ID, Service_Name, Start_Date, End_Date, Price)
VALUES 
    (1001, 'Grooming', '2024-11-01', '2024-11-30', 500.00),
    (1002, 'Checkups', '2024-11-01', '2024-11-25', 350.00),
    (1003, 'Vaccinations', '2024-11-20', '2024-11-25', 1250.00),
    (1004, 'Boarding', '2024-11-01', '2024-11-30', 100.00);
    
INSERT INTO Owner (Owner_ID, First_Name, Last_Name, Contact_Info, City, Street, Postal_Code)
VALUES
    (NULL, 'Ethan', 'Dela Cruz', '09171302924', 'Pasig', 'Manchester', 1611),
    (NULL, 'Christopher', 'Mateo', '09324958134', 'Baguio', 'Sunset Blvd', 2600),
    (NULL, 'Djermeyn', 'Wasan', '098313215', 'Marikina', 'Michigan Avenue', 1800),
    (NULL, 'Bobby', 'Brown', '0991231513', 'Manila', 'Taft', 6816),
    (NULL, 'Charlie', 'Green', '0918283131', 'Pasay', 'Market Street', 1300);
    
INSERT INTO Pet (Pet_ID, Pet_Name, Breed, Age, HealthInformation, Owner_ID)
VALUES
    (NULL, 'Cream', 'Shih Tzu', 1, 'Healthy', 1000),
    (NULL, 'Maxey', 'Bulldog', 5, 'Healthy', 1001),
    (NULL, 'Twix', 'Poodle', 2, 'Vaccinated', 1002),
    (NULL, 'Coco', 'Golden Retriever', 4, 'Allergic to pollen', 1003),
    (NULL,'Rocky', 'British Shorthair', 6, 'Requires daily medication', 1004);

    
INSERT INTO Services_Transaction (Transaction_ID, Service_ID, Owner_ID, Transaction_Date, Quantity)
VALUES
    (1, 1001, 1001, '2024-11-15', 1),  -- Grooming for christopher’s dog
    (2, 1002, 1003, '2024-11-10', 1),  -- Checkup for djermeyns dog
    (3, 1003, 1003, '2024-11-21', 1),  -- Vaccinations for djermeyns dog
    (4, 1004, 1004, '2024-11-20', 3),  -- Boarding for bobbys cat
    (5, 1004, 1001, '2024-11-15', 2),  -- Boarding for ethans dog
    (6, 1001, 1004, '2024-11-18', 1);  -- Grooming for bobbys cat

CREATE TABLE Employees (
Employee_ID INT PRIMARY KEY,                -- Unique identifier for each employee
	First_Name VARCHAR(30) NOT NULL,    	-- First name of the employee
	Last_Name VARCHAR(30) NOT NULL,    	-- Last name of the employee
	Position VARCHAR(30),                  		-- Job position/title
	Phone_Number VARCHAR(15),               	-- Contact number
	Email VARCHAR(100)                     		-- Contact email
);


INSERT INTO Employees (Employee_ID, First_Name, Last_Name, Position, Phone_Number, Email)
VALUES
(101, 'John', 'Doe', 'Groomer', '09171234567', 'john.doe@example.com'),
(102, 'Jane', 'Smith', 'Veterinarian', '09229876543', 'jane.smith@example.com'),
(103, 'Emily', 'Johnson', 'Receptionist', '09395678910', 'emily.johnson@example.com'),
(104, 'Michael', 'Brown', 'Caretaker', '09452345678', 'michael.brown@example.com'),
(105, 'Sarah', 'Lucas', 'Cashier', '09156781234', 'sarah.lucas@example.com');


CREATE TABLE Appointment_Record (
    Appointment_ID INT AUTO_INCREMENT PRIMARY KEY, -- Unique identifier for each appointment
    Appointment_Date DATE NOT NULL,                -- Date of the appointment
    PetID INT NOT NULL,                           -- Foreign key referencing the Pet table
    PetName VARCHAR(30),                          -- Name of the pet
    ServiceID INT NOT NULL,                       -- Foreign key referencing the Service table
    EmployeeID INT NOT NULL,                      -- Foreign key referencing the Employee table
   
    -- Foreign key constraints
    CONSTRAINT FK_Pet FOREIGN KEY (PetID) REFERENCES Pet(Pet_ID),
    CONSTRAINT FK_Service FOREIGN KEY (ServiceID) REFERENCES Services(Service_ID),
    CONSTRAINT FK_Employee FOREIGN KEY (EmployeeID) REFERENCES Employees(Employee_ID)
) AUTO_INCREMENT = 100;
INSERT INTO Appointment_Record (Appointment_ID, Appointment_Date, PetID, PetName, ServiceID, EmployeeID)
VALUES
    (NULL, '2024-11-10', 10003, 'Coco', 1002, 102),
    (NULL, '2024-11-15', 10000, 'Cream', 1001, 101),
    (NULL, '2024-11-15', 10000, 'Cream', 1004, 104),
    (NULL, '2024-11-18', 10004, 'Rocky', 1001, 101),
    (NULL, '2024-11-20', 10004, 'Rocky', 1004, 104),
    (NULL, '2024-11-21', 10003, 'Coco', 1003, 102); 


SELECT  st.Transaction_ID, st.Service_ID, o.First_Name, o.Last_Name, st.Owner_ID, st.Transaction_Date, st.Quantity, s.Price, (st.Quantity * s.Price) AS Total_Amount
FROM Services_Transaction st
JOIN Services s ON st.Service_ID = s.Service_ID
JOIN Owner o ON st.Owner_ID = o.Owner_ID;

SELECT * FROM Product;
SELECT * FROM Inventory;
SELECT * FROM sales_transaction;
SELECT * FROM Services;
SELECT * FROM Owner;
SELECT * FROM Pet;
SELECT * FROM Services_Transactions;
SELECT * FROM Employees;
SELECT * FROM Appointment_Record;
