create table if not EXISTS event (
	id INT primary key AUTO_INCREMENT,
	title varchar(50) NOT NULL,
    date datetime NOT NULL,
    ticketPrice decimal(20,2) NOT NULL
);

create table if not EXISTS user (
	id INT primary key AUTO_INCREMENT,
    name varchar(50) not null,
    email varchar(50) not null unique
);

create table if not EXISTS ticket (
	id INT primary key AUTO_INCREMENT,
    uid INT NOT NULL,
    eid INT NOT NULL,
    category varchar(50) NOT NULL,
    place INT NOT NULL ,
	FOREIGN KEY (uid) REFERENCES user (id)
       ON DELETE CASCADE,
	FOREIGN KEY (eid) REFERENCES event (id)
       ON DELETE CASCADE
);

create table if not EXISTS userAccount (
	id INT primary key AUTO_INCREMENT,
    prePaidAmount decimal(20,2) NOT NULL,
    userId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES user (id)
       ON DELETE CASCADE
);

