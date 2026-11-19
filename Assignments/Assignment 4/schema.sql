CREATE TABLE registrations (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 student_id VARCHAR(30) NOT NULL,
 event_name VARCHAR(100) NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 UNIQUE KEY uq_student_event(student_id,event_name)
);