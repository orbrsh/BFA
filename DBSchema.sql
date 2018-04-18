CREATE TABLE CUSTOMER(Username varchar(10), Password varchar(8), Address varchar(100), Nickname varchar(20), Description varchar(50), Photo varchar(50), Email varchar(100), Phone varchar(10), isActive INT DEFAULT 1, PRIMARY KEY (Username), UNIQUE (Email,Nickname));

INSERT INTO CUSTOMER (Username, Password, Address, Nickname, Description, Photo, Email, Phone) VALUES(?,?,?,?,?,?,?,?);
SELECT * FROM CUSTOMER WHERE isActive = 1;
SELECT * FROM CUSSELECT * FROM CUSTOMER WHERE Username=? OR Email=? OR Nickname=?;
UPDATE CUSTOMER SET isActive=0 WHERE Username=?;


CREATE TABLE BOOKS (IdBooks INT,Name varchar(100),Author varchar(100),Photo varchar(100),Price REAL,Description varchar(650),FullHtml varchar(300),PRIMARY KEY (IdBooks));

INSERT INTO BOOKS (IdBooks, Name, Author, Photo, Price, Description, FullHtml) VALUES(?,?,?,?,?,?,?);
SELECT * FROM BOOKS;
SELECT * FROM BOOKS WHERE Name=?;
SELECT * FROM BOOKS WHERE IdBooks=?;


CREATE TABLE LIKES(IdBook INT,Username varchar(10),dateSetTimeStamp BIGINT,PRIMARY KEY (IdBook, Username));

INSERT INTO LIKES (IdBook, Username, dateSetTimeStamp) VALUES(?,?,?);
SELECT * FROM LIKES;
SELECT * FROM LIKES WHERE Username=?;
SELECT * FROM LIKES WHERE Username=? AND IdBook=?;
SELECT * FROM LIKES WHERE IdBook=?;
DELETE FROM LIKES WHERE Username=? AND IdBook=?;


CREATE TABLE REVIEWS(IdReviews INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), IdBook INT, Username varchar(10),dateWritten BIGINT,dateApproved BIGINT DEFAULT 0,isApproved INT DEFAULT 0,reviewText varchar(650),PRIMARY KEY(IdReviews, IdBook, Username));

INSERT INTO REVIEWS (IdBook, Username, dateWritten, dateApproved, isApproved, reviewText) VALUES(?,?,?,?,?,?);
INSERT INTO REVIEWS (IdBook, Username, dateWritten, reviewText) VALUES(?,?,?,?);
SELECT * FROM REVIEWS;
SELECT * FROM REVIEWS WHERE IdBook=?;
SELECT * FROM REVIEWS WHERE IdBook=? AND isApproved=1;
SELECT * FROM REVIEWS WHERE isApproved=0;
SELECT * FROM REVIEWS WHERE Username=?;
UPDATE REVIEWS SET isApproved = 1 WHERE IdReviews=?;


CREATE TABLE PURCHASES(IdPurchase INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),IdBook INT NOT NULL,Username varchar(10) NOT NULL,DateBought BIGINT, location BIGINT DEFAULT 0, PRIMARY KEY(IdPurchase, IdBook, Username));

INSERT INTO PURCHASES (IdBook, Username, DateBought) VALUES(?,?,?);
SELECT * FROM PURCHASES;
SELECT * FROM PURCHASES WHERE Username=?;
SELECT * FROM PURCHASES WHERE IdBook=?;
SELECT * FROM PURCHASES WHERE Username=? AND IdBook=?;
UPDATE PURCHASES SET location = ? WHERE IdBook=? AND Username=?;
SELECT IdBook, Username, location FROM PURCHASES WHERE IdBook=? AND Username=?;