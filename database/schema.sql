-- ============================================================
-- ExploraVida 1.0.0 - esquema de base de datos
-- SQLite generado por Room (version 1, archivo exploravida.db)
--
-- 17 tablas: 10 de contenido educativo y 7 de progreso del nino.
-- Todo es local: no hay usuarios remotos, ni correo, ni identificadores
-- personales. El unico perfil posible es el alias elegido en el onboarding.
-- ============================================================

PRAGMA foreign_keys = ON;

-- ------------------------------------------------------------
-- CONTENIDO EDUCATIVO (se siembra al crear la base)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS living_system (
    id               TEXT    NOT NULL PRIMARY KEY,
    name             TEXT    NOT NULL,
    shortDescription TEXT    NOT NULL,
    colorHex         TEXT    NOT NULL,
    iconKey          TEXT    NOT NULL,
    orderIndex       INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS system_connection (
    id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    fromSystemId TEXT    NOT NULL,
    toSystemId   TEXT    NOT NULL,
    explanation  TEXT    NOT NULL,
    FOREIGN KEY (fromSystemId) REFERENCES living_system(id) ON DELETE CASCADE,
    FOREIGN KEY (toSystemId)   REFERENCES living_system(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_system_connection_from_to
    ON system_connection (fromSystemId, toSystemId);
CREATE INDEX IF NOT EXISTS index_system_connection_toSystemId
    ON system_connection (toSystemId);

CREATE TABLE IF NOT EXISTS learning_experience (
    id            TEXT    NOT NULL PRIMARY KEY,
    title         TEXT    NOT NULL,
    subtitle      TEXT    NOT NULL,
    noraIntro     TEXT    NOT NULL,
    kind          TEXT    NOT NULL,   -- RECORRIDO, EXPLORACION, SECUENCIA, CONEXION, COMPARACION, HISTORIA
    orderIndex    INTEGER NOT NULL,
    requiredXp    INTEGER NOT NULL,   -- XP necesario para desbloquearla
    backgroundKey TEXT    NOT NULL,   -- uno de los 10 fondos ilustrados
    iconKey       TEXT    NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS index_learning_experience_orderIndex
    ON learning_experience (orderIndex);

CREATE TABLE IF NOT EXISTS experience_step (
    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    experienceId    TEXT    NOT NULL,
    orderIndex      INTEGER NOT NULL,
    title           TEXT    NOT NULL,
    text            TEXT    NOT NULL,
    systemId        TEXT,              -- puede ser NULL: hay pasos generales
    animationKey    TEXT    NOT NULL,  -- una de las 20 animaciones
    illustrationKey TEXT    NOT NULL,  -- una de las 30 ilustraciones
    FOREIGN KEY (experienceId) REFERENCES learning_experience(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_experience_step_experienceId_orderIndex
    ON experience_step (experienceId, orderIndex);

CREATE TABLE IF NOT EXISTS interactive_element (
    id              TEXT NOT NULL PRIMARY KEY,
    name            TEXT NOT NULL,
    systemId        TEXT NOT NULL,
    description     TEXT NOT NULL,
    x               REAL NOT NULL,   -- coordenada normalizada 0..1 sobre Vita
    y               REAL NOT NULL,
    radius          REAL NOT NULL,
    illustrationKey TEXT NOT NULL,
    FOREIGN KEY (systemId) REFERENCES living_system(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_interactive_element_systemId
    ON interactive_element (systemId);

CREATE TABLE IF NOT EXISTS activity (
    id           TEXT    NOT NULL PRIMARY KEY,
    experienceId TEXT    NOT NULL,
    kind         TEXT    NOT NULL,  -- ARRASTRAR, ORDENAR, CONECTAR, COMPARAR, PREDECIR, OBSERVAR
    title        TEXT    NOT NULL,
    prompt       TEXT    NOT NULL,
    situation    TEXT    NOT NULL,  -- situacion cotidiana que da sentido al reto
    difficulty   INTEGER NOT NULL,
    xpReward     INTEGER NOT NULL,
    FOREIGN KEY (experienceId) REFERENCES learning_experience(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_activity_experienceId ON activity (experienceId);

CREATE TABLE IF NOT EXISTS sequence (
    id          TEXT NOT NULL PRIMARY KEY,
    activityId  TEXT NOT NULL,
    title       TEXT NOT NULL,
    explanation TEXT NOT NULL,
    FOREIGN KEY (activityId) REFERENCES activity(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_sequence_activityId ON sequence (activityId);

CREATE TABLE IF NOT EXISTS sequence_item (
    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    sequenceId      TEXT    NOT NULL,
    label           TEXT    NOT NULL,
    correctPosition INTEGER NOT NULL,
    systemId        TEXT,
    illustrationKey TEXT    NOT NULL,
    FOREIGN KEY (sequenceId) REFERENCES sequence(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_sequence_item_sequenceId_correctPosition
    ON sequence_item (sequenceId, correctPosition);

CREATE TABLE IF NOT EXISTS connection_challenge (
    id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    activityId   TEXT    NOT NULL,
    fromSystemId TEXT    NOT NULL,
    toSystemId   TEXT    NOT NULL,
    explanation  TEXT    NOT NULL,
    FOREIGN KEY (activityId) REFERENCES activity(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_connection_challenge_activity_from_to
    ON connection_challenge (activityId, fromSystemId, toSystemId);

CREATE TABLE IF NOT EXISTS badge (
    id          TEXT    NOT NULL PRIMARY KEY,
    name        TEXT    NOT NULL,
    description TEXT    NOT NULL,
    iconKey     TEXT    NOT NULL,
    ruleKey     TEXT    NOT NULL,  -- regla que evalua RewardEngine
    threshold   INTEGER NOT NULL
);

-- ------------------------------------------------------------
-- PROGRESO DEL NINO (se crea al terminar el onboarding)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS user_profile (
    id             INTEGER NOT NULL PRIMARY KEY,  -- siempre 1: un solo perfil local
    alias          TEXT    NOT NULL,              -- alias elegido, nunca el nombre real
    avatarId       INTEGER NOT NULL,              -- 0..7, ilustraciones locales
    soundEnabled   INTEGER NOT NULL DEFAULT 1,
    hapticsEnabled INTEGER NOT NULL DEFAULT 1,
    onboardingDone INTEGER NOT NULL DEFAULT 0,
    createdAt      INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS progress (
    id                   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    profileId            INTEGER NOT NULL DEFAULT 1,
    xp                   INTEGER NOT NULL DEFAULT 0,
    level                INTEGER NOT NULL DEFAULT 1,
    experiencesCompleted INTEGER NOT NULL DEFAULT 0,
    activitiesCompleted  INTEGER NOT NULL DEFAULT 0,
    perfectActivities    INTEGER NOT NULL DEFAULT 0,
    journeysCompleted    INTEGER NOT NULL DEFAULT 0,
    sequencesSolved      INTEGER NOT NULL DEFAULT 0,
    connectionsMade      INTEGER NOT NULL DEFAULT 0,
    elementsExplored     INTEGER NOT NULL DEFAULT 0,
    discoveries          INTEGER NOT NULL DEFAULT 0,
    visitedSystems       TEXT    NOT NULL DEFAULT '',  -- lista separada por |
    updatedAt            INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (profileId) REFERENCES user_profile(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_progress_profileId ON progress (profileId);

CREATE TABLE IF NOT EXISTS activity_attempt (
    id            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    activityId    TEXT    NOT NULL,
    profileId     INTEGER NOT NULL DEFAULT 1,
    correct       INTEGER NOT NULL,
    stars         INTEGER NOT NULL,
    attemptNumber INTEGER NOT NULL,
    detail        TEXT    NOT NULL,
    attemptedAt   INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (activityId) REFERENCES activity(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_activity_attempt_activityId ON activity_attempt (activityId);
CREATE INDEX IF NOT EXISTS index_activity_attempt_profileId  ON activity_attempt (profileId);

CREATE TABLE IF NOT EXISTS explorer_notebook (
    id         TEXT    NOT NULL PRIMARY KEY,
    profileId  INTEGER NOT NULL DEFAULT 1,
    title      TEXT    NOT NULL,
    pageIndex  INTEGER NOT NULL,
    stickerKey TEXT    NOT NULL,
    unlocked   INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (profileId) REFERENCES user_profile(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_explorer_notebook_profileId_pageIndex
    ON explorer_notebook (profileId, pageIndex);

CREATE TABLE IF NOT EXISTS notebook_discovery (
    id           INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    notebookId   TEXT    NOT NULL,
    conceptKey   TEXT    NOT NULL,
    text         TEXT    NOT NULL,
    stickerKey   TEXT    NOT NULL,
    discoveredAt INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (notebookId) REFERENCES explorer_notebook(id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS index_notebook_discovery_notebookId
    ON notebook_discovery (notebookId);
CREATE UNIQUE INDEX IF NOT EXISTS index_notebook_discovery_conceptKey
    ON notebook_discovery (conceptKey);

CREATE TABLE IF NOT EXISTS user_badge (
    id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    badgeId   TEXT    NOT NULL,
    profileId INTEGER NOT NULL DEFAULT 1,
    earnedAt  INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (badgeId) REFERENCES badge(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_user_badge_badgeId_profileId
    ON user_badge (badgeId, profileId);

CREATE TABLE IF NOT EXISTS unlocked_experience (
    id               INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    experienceId     TEXT    NOT NULL,
    profileId        INTEGER NOT NULL DEFAULT 1,
    started          INTEGER NOT NULL DEFAULT 0,
    completed        INTEGER NOT NULL DEFAULT 0,
    mastered         INTEGER NOT NULL DEFAULT 0,
    exploredElements TEXT    NOT NULL DEFAULT '',  -- lista separada por |
    unlockedAt       INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (experienceId) REFERENCES learning_experience(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX IF NOT EXISTS index_unlocked_experience_experienceId_profileId
    ON unlocked_experience (experienceId, profileId);
