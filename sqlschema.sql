drop database if exists library;
create database library;
use library;

/* boot table */
create table book(
book_id char(10) primary key not null,
title varchar(150)
);

/* Book Authors table */
create table book_authors(
book_id char(10),
author_name varchar(50),
type integer(1),

constraint pk_book_id_author primary key (book_id, author_name),
constraint fk_book_authors foreign key (book_id) references book(book_id)
);

/* Library Branch table */
create table library_branch(
branch_id integer primary key not null,
branch_name varchar(50),
address varchar(100)
);

/* book copies table */
create table book_copies (
book_id char(10),
branch_id integer,
no_of_copies integer,

constraint book_id_book_copies foreign key (book_id) references book(book_id),
constraint branch_id_book_copies foreign key (branch_id) references library_branch(branch_id)
);

/* borrower table */
create table borrower (
card_no integer primary key not null,
fname varchar(25),
lname varchar(25),
address varchar(50),
city varchar (20),
state varchar(20),
phone varchar(20)
);

/* book_loans */
create table book_loans (
loan_id integer primary key not null,
book_id char(10),
branch_id integer,
card_no integer,
date_out date,
due_date date,
date_in date,

constraint book_id_book_loans foreign key (book_id) references book(book_id),
constraint branch_id_book_loans foreign key (branch_id) references library_branch(branch_id),
constraint card_no_book_loans foreign key (card_no) references borrower(card_no)
);

/* Library Fines */
create table fines (
loan_id integer unique not null,
fine_amt decimal(10,2),
paid boolean default false,

constraint loan_id_fines foreign key (loan_id) references book_loans(loan_id)
);

/* NextValue table */
create table nextvalue (
name varchar(20),
nextval integer
);

/* Create Fines view */
create view FineView as (select bl.loan_id, bl.card_no, (datediff(bl.date_in, bl.due_date) * 0.25) as FineAmount, f.paid, bl.date_in from book_loans bl left join fines f on f.loan_id = bl.loan_id where bl.date_in is not null having FineAmount > 0)
union
((select bl.loan_id, bl.card_no, (datediff(curdate(), bl.due_date) * 0.25) as FineAmount, f.paid, bl.date_in from book_loans bl left join fines f on f.loan_id = bl.loan_id where bl.date_in is null having FineAmount > 0))
;

/* user name and password table */
create table userinfo ( userid varchar(50), password varchar(50));