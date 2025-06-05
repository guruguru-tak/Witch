CREATE TABLE `user`
(
    `user_id`           VARCHAR(36)         NOT NULL PRIMARY KEY,
    `email`             VARCHAR(320) UNIQUE NOT NULL,
    `nickname`          VARCHAR(12) UNIQUE  NOT NULL,
    `password`          VARCHAR(60)         NOT NULL,
    `profile_image_url` VARCHAR(2000)       NULL,
    `created_at`        TIMESTAMP           NOT NULL,
    `modified_at`       TIMESTAMP           NOT NULL
);

CREATE TABLE `group`
(
    `group_id`        VARCHAR(36)        NOT NULL PRIMARY KEY,
    `name`            VARCHAR(20) UNIQUE NOT NULL,
    `group_image_url` VARCHAR(2000)      NULL,
    `created_at`      TIMESTAMP          NOT NULL,
    `modified_at`     TIMESTAMP          NOT NULL
);

CREATE TABLE `appointment`
(
    `appointment_id`   VARCHAR(36)  NOT NULL PRIMARY KEY,
    `group_id`         VARCHAR(36)  NOT NULL,
    `name`             VARCHAR(20)  NOT NULL,
    `summary`          TEXT         NULL,
    `appointment_time` TIMESTAMP    NOT NULL,
    `status`           VARCHAR(10)  NOT NULL comment 'PENDING/ACTIVE/INACTIVE',
    `latitude`         DOUBLE       NOT NULL,
    `longitude`        DOUBLE       NOT NULL,
    `created_at`       TIMESTAMP    NOT NULL,
    `modified_at`      TIMESTAMP    NOT NULL,
    `address`          VARCHAR(255) NOT NULL
);

CREATE TABLE `group_member`
(
    `group_member_id`  VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`          VARCHAR(36) NOT NULL,
    `group_id`         VARCHAR(36) NOT NULL,
    `is_leader`        TINYINT(1)  NOT NULL,
    `created_at`       TIMESTAMP   NOT NULL,
    `modified_at`      TIMESTAMP   NOT NULL,
    `cnt_late_arrival` INT         NOT NULL
);

CREATE TABLE `appointment_member`
(
    `appointment_member_id` VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`               VARCHAR(36) NOT NULL,
    `appointment_id`        VARCHAR(36) NOT NULL,
    `is_leader`             TINYINT(1)  NOT NULL,
    `final_latitude`        DOUBLE      NULL comment '약속 종료 후 최종 위도',
    `final_longitude`       DOUBLE      NULL comment '약속 종료 후 최종 경도',
    `created_at`            TIMESTAMP   NOT NULL,
    `modified_at`           TIMESTAMP   NOT NULL
);

CREATE TABLE `group_join_request`
(
    `group_join_request_id` VARCHAR(36) NOT NULL PRIMARY KEY,
    `user_id`               VARCHAR(36) NOT NULL,
    `group_id`              VARCHAR(36) NOT NULL,
    `created_at`            TIMESTAMP   NOT NULL,
    `modified_at`           TIMESTAMP   NOT NULL
);

CREATE TABLE `snack`
(
    `snack_id`        VARCHAR(36)   NOT NULL PRIMARY KEY,
    `appointment_id`  VARCHAR(36)   NOT NULL,
    `user_id`         VARCHAR(36)   NOT NULL,
    `latitude`        DOUBLE        NOT NULL,
    `longitude`       DOUBLE        NOT NULL,
    `snack_image_url` VARCHAR(2000) NULL,
    `snack_sound_url` VARCHAR(2000) NULL,
    `created_at`      TIMESTAMP     NOT NULL,
    `modified_at`     TIMESTAMP     NOT NULL
);

CREATE TABLE `fcm_token`
(
    `fcm_token_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`      VARCHAR(36)  NOT NULL,
    `fcm_token`    VARCHAR(300) NOT NULL
);
