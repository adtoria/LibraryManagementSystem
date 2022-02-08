CREATE TABLE bookRecord ( 
	bookId VARCHAR(20) NOT NULL PRIMARY KEY,
	bookName VARCHAR(20) NOT NULL,
	authorName VARCHAR(20) NOT NULL,
	issueStatus INT NOT NULL,
	issuedBy VARCHAR(20)
);

CREATE TABLE studentRecord (
	studentId VARCHAR(20) NOT NULL PRIMARY KEY,
	studentName VARCHAR(20) NOT NULL,
	bookCount INT
);

CREATE TABLE transactionList (
	recordId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    studentId VARCHAR(20),
    bookId INT,
    issueDate DATE,
    fine INT
);

SELECT * FROM bookRecord;
SELECT * FROM studentRecord;
SELECT * FROM issueList;