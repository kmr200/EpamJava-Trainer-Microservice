INSERT INTO trainer (username, first_name, last_name, is_active)
VALUES
    ('michael.wilson', 'Michael', 'Wilson', true),
    ('olivia.moore', 'Olivia', 'Moore', true),
    ('james.taylor', 'James', 'Taylor', false),
    ('sophia.anderson', 'Sophia', 'Anderson', true),
    ('liam.thomas', 'Liam', 'Thomas', true);

INSERT INTO training (training_uuid, trainer_username, training_date, training_duration)
VALUES
    ('2f0181f1-6b81-4e09-9846-da83eea26f63', 'michael.wilson', '2024-09-15', 60),
    ('a5415a8d-30d6-4b08-86bd-a25232f34a85', 'olivia.moore', '2024-09-16', 45),
    ('496d4de1-f298-48fb-b236-ec15aba68a6e', 'james.taylor', '2024-09-17', 90),
    ('41c194d6-8e41-4cc5-80c0-1f126f67f3c4', 'sophia.anderson', '2024-09-18', 30),
    ('bb8e0c09-e611-4011-a0ec-32b19ff6bce7', 'liam.thomas', '2024-09-19', 60);
