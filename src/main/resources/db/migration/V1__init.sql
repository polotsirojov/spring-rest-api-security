create table users(
    id serial PRIMARY KEY,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    username varchar(50) unique not null,
    password varchar(100) not null,
    role varchar(20) not null,
    is_active bool not null
);

create table trainee(
    id serial PRIMARY KEY,
    dob date,
    address varchar(255),
    user_id int unique not null,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
);

create table training_type(
    id serial PRIMARY KEY,
    name varchar(255)
);

create table trainer(
    id serial PRIMARY KEY,
    specialization_id int not null,
    user_id int unique not null,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_training_type FOREIGN KEY(specialization_id) REFERENCES training_type(id)
);

create table training(
    id serial PRIMARY KEY,
    trainee_id int not null,
    trainer_id int not null,
    training_name varchar(50) not null,
    training_type_id int not null,
    training_date date not null,
    training_duration int not null,
    CONSTRAINT fk_trainee FOREIGN KEY(trainee_id) REFERENCES trainee(id) ON DELETE CASCADE,
    CONSTRAINT fk_trainer FOREIGN KEY(trainer_id) REFERENCES trainer(id),
    CONSTRAINT fk_training_type FOREIGN KEY(training_type_id) REFERENCES training_type(id)
);

insert into training_type (id,name)
    values (1,'Push-up'),
    (2,'Weightlifting'),
    (3,'Squats'),
    (4,'Aerobics');