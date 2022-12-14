CREATE TABLE workout_details (
    workout_id  INTEGER,
    exercise_id INTEGER,
    set_num     INTEGER,
    weight      REAL    NOT NULL CHECK (weight >= 0),
    reps        INTEGER NOT NULL CHECK (reps >= 0),
    PRIMARY KEY (workout_id, exercise_id, set_num),
    FOREIGN KEY (workout_id)
        REFERENCES workout (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT,
    FOREIGN KEY (exercise_id)
        REFERENCES exercise (id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);
