# --- !Ups

create table "POMODORO" ("ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"TIME" TIMESTAMP NOT NULL,"TASK_ID" BIGINT);

# --- !Downs

drop table "POMODORO";