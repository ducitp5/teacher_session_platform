-- Rename sessions table to course_sessions
RENAME TABLE sessions TO course_sessions;

-- If the above fails in some MySQL versions or depending on constraints, 
-- we might need to drop and recreate constraints, but usually RENAME TABLE handles it.
-- However, just to be safe and explicit:
-- ALTER TABLE enrollments DROP FOREIGN KEY enrollments_ibfk_1;
-- ALTER TABLE enrollments ADD CONSTRAINT fk_enrollments_session FOREIGN KEY (session_id) REFERENCES course_sessions(id) ON DELETE CASCADE;
