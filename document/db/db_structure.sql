-- teacher_session_platform.flyway_schema_history definition

CREATE TABLE `flyway_schema_history`
(
    `installed_rank` int           NOT NULL,
    `version`        varchar(50)            DEFAULT NULL,
    `description`    varchar(200)  NOT NULL,
    `type`           varchar(20)   NOT NULL,
    `script`         varchar(1000) NOT NULL,
    `checksum`       int                    DEFAULT NULL,
    `installed_by`   varchar(100)  NOT NULL,
    `installed_on`   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` int           NOT NULL,
    `success`        tinyint(1) NOT NULL,
    PRIMARY KEY (`installed_rank`),
    KEY              `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- teacher_session_platform.users definition

CREATE TABLE `users`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `email`      varchar(255) NOT NULL,
    `password`   varchar(255) NOT NULL,
    `first_name` varchar(100) NOT NULL,
    `last_name`  varchar(100) NOT NULL,
    `role`       varchar(50)  NOT NULL,
    `status`     varchar(50)  NOT NULL DEFAULT 'ACTIVE',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- teacher_session_platform.course_sessions definition

CREATE TABLE `course_sessions`
(
    `id`                bigint         NOT NULL AUTO_INCREMENT,
    `teacher_id`        bigint         NOT NULL,
    `title`             varchar(255)   NOT NULL,
    `description`       text           NOT NULL,
    `subject`           varchar(100)   NOT NULL,
    `price`             decimal(10, 2) NOT NULL,
    `max_students`      int            NOT NULL,
    `enrolled_students` int                     DEFAULT '0',
    `session_type`      varchar(50)    NOT NULL,
    `location`          varchar(255)            DEFAULT NULL,
    `meeting_link`      varchar(255)            DEFAULT NULL,
    `start_date`        datetime       NOT NULL,
    `duration_minutes`  int            NOT NULL,
    `status`            varchar(50)    NOT NULL DEFAULT 'OPEN',
    `created_at`        timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY                 `teacher_id` (`teacher_id`),
    CONSTRAINT `course_sessions_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- teacher_session_platform.enrollments definition

CREATE TABLE `enrollments`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `session_id`  bigint      NOT NULL,
    `student_id`  bigint      NOT NULL,
    `status`      varchar(50) NOT NULL DEFAULT 'ACTIVE',
    `enrolled_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `session_id` (`session_id`,`student_id`),
    KEY           `student_id` (`student_id`),
    CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `course_sessions` (`id`) ON DELETE CASCADE,
    CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- teacher_session_platform.teacher_profiles definition

CREATE TABLE `teacher_profiles`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `user_id`        bigint NOT NULL,
    `bio`            text,
    `expertise_area` varchar(255)  DEFAULT NULL,
    `rating_average` decimal(3, 2) DEFAULT '0.00',
    PRIMARY KEY (`id`),
    KEY              `user_id` (`user_id`),
    CONSTRAINT `teacher_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;