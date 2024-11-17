
CREATE TABLE trainer
(
    username VARCHAR(100) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE training
(
    training_uuid VARCHAR(36) PRIMARY KEY,
    trainer_username VARCHAR(100) NOT NULL,
    training_date DATE NOT NULL,
    training_duration INT NOT NULL
);