INSERT INTO specialty (name) VALUES 
('Cardiología'),
('Dermatología'),
('Pediatría'),
('Ortopedia'),
('Ginecología'),
('Neurología'),
('Oftalmología'),
('Otorrinolaringología');

INSERT INTO doctor (name, last_name, specialty_id, schedule_start, schedule_end) VALUES 
('Juan', 'Pérez', 1, '08:00:00', '12:00:00'),
('María', 'Gómez', 2, '09:00:00', '13:00:00'),
('Carlos', 'López', 3, '10:00:00', '14:00:00'),
('Ana', 'Martínez', 4, '11:00:00', '15:00:00'),
('Luis', 'Hernández', 5, '12:00:00', '16:00:00'),
('Laura', 'Ramírez', 6, '13:00:00', '17:00:00'),
('Pedro', 'Torres', 7, '14:00:00', '18:00:00'),
('Sofía', 'Vásquez', 8, '15:00:00', '19:00:00');

INSERT INTO patient (name, last_name, birth_date, address, phone, email) VALUES 
('Alejandro', 'Ramírez', '1985-03-15', 'Calle 123 #45-67', '3101234567', 'alejandro@email.com'),
('Beatriz', 'González', '1990-07-22', 'Avenida 5 #23-10', '3202345678', 'beatriz@email.com'),
('Carlos', 'Hernández', '1978-11-30', 'Carrera 7 #8-90', '3003456789', 'carlos@email.com'),
('Diana', 'Torres', '1995-05-10', 'Calle 34 #12-34', '3154567890', 'diana@email.com'),
('Eduardo', 'Vargas', '1982-09-18', 'Avenida 2 #67-89', '3185678901', 'eduardo@email.com'),
('Fernanda', 'Jiménez', '1988-12-05', 'Carrera 10 #45-67', '3136789012', 'fernanda@email.com'),
('Gabriel', 'Moreno', '1975-04-25', 'Calle 56 #78-90', '3177890123', 'gabriel@email.com'),
('Hilda', 'Díaz', '1992-08-12', 'Avenida 8 #34-56', '3148901234', 'hilda@email.com');

INSERT INTO appointment (patient_id, doctor_id, date_time, status) VALUES 
(1, 3, '2023-11-15 10:00:00', 'COMPLETED'),
(2, 5, '2023-11-16 14:30:00', 'SCHEDULED'),
(3, 2, '2023-11-17 11:15:00', 'SCHEDULED'),
(4, 1, '2023-11-18 09:00:00', 'CANCELLED'),
(5, 4, '2023-11-19 16:45:00', 'SCHEDULED'),
(6, 7, '2023-11-20 08:30:00', 'SCHEDULED'),
(7, 6, '2023-11-21 13:00:00', 'SCHEDULED'),
(8, 8, '2023-11-22 15:20:00', 'SCHEDULED');


INSERT INTO specialty (name) VALUES 
('Cardiología'),
('Dermatología'),
('Pediatría'),
('Ortopedia'),
('Ginecología'),
('Neurología'),
('Oftalmología'),
('Otorrinolaringología');

INSERT INTO doctor (name, last_name, specialty_id, schedule_start, schedule_end) VALUES 
('Carlos', 'Gómez', 1, '08:00:00', '16:00:00'),
('María', 'López', 2, '09:00:00', '17:00:00'),
('Juan', 'Martínez', 3, '08:30:00', '15:30:00'),
('Ana', 'Rodríguez', 4, '10:00:00', '18:00:00'),
('Luis', 'Fernández', 5, '07:00:00', '14:00:00'),
('Sofía', 'Pérez', 6, '13:00:00', '20:00:00'),
('Pedro', 'García', 7, '08:00:00', '12:00:00'),
('Laura', 'Sánchez', 8, '14:00:00', '21:00:00');

INSERT INTO patient (name, last_name, birth_date, address, phone, email) VALUES 
('Alejandro', 'Ramírez', '1985-03-15', 'Calle 123 #45-67', '3101234567', 'alejandro@email.com'),
('Beatriz', 'González', '1990-07-22', 'Avenida 5 #23-10', '3202345678', 'beatriz@email.com'),
('Carlos', 'Hernández', '1978-11-30', 'Carrera 7 #8-90', '3003456789', 'carlos@email.com'),
('Diana', 'Torres', '1995-05-10', 'Calle 34 #12-34', '3154567890', 'diana@email.com'),
('Eduardo', 'Vargas', '1982-09-18', 'Avenida 2 #67-89', '3185678901', 'eduardo@email.com'),
('Fernanda', 'Jiménez', '1988-12-05', 'Carrera 10 #45-67', '3136789012', 'fernanda@email.com'),
('Gabriel', 'Moreno', '1975-04-25', 'Calle 56 #78-90', '3177890123', 'gabriel@email.com'),
('Hilda', 'Díaz', '1992-08-12', 'Avenida 8 #34-56', '3148901234', 'hilda@email.com');

INSERT INTO appointment (patient_id, doctor_id, date_time, status) VALUES 
(1, 3, '2023-11-15 10:00:00', 'COMPLETED'),
(2, 5, '2023-11-16 14:30:00', 'SCHEDULED'),
(3, 2, '2023-11-17 11:15:00', 'SCHEDULED'),
(4, 1, '2023-11-18 09:00:00', 'CANCELLED'),
(5, 4, '2023-11-19 16:45:00', 'SCHEDULED'),
(6, 7, '2023-11-20 08:30:00', 'SCHEDULED'),
(7, 6, '2023-11-21 13:00:00', 'SCHEDULED'),
(8, 8, '2023-11-22 15:20:00', 'SCHEDULED');
