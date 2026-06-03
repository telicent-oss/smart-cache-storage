--
-- Copyright (C) Telicent Ltd
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE SEQUENCE IF NOT EXISTS DISTRIBUTION_STATES_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS LIFECYCLE_ACTIONS_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE DISTRIBUTION_STATES
(
    id             BIGINT       NOT NULL,
    distributionId VARCHAR(500) NOT NULL,
    state          SMALLINT     NOT NULL,
    CONSTRAINT pk_distribution_states PRIMARY KEY (id)
);

CREATE TABLE LIFECYCLE_ACTIONS
(
    id      BIGINT NOT NULL,
    eventId UUID,
    action  JSON   NOT NULL,
    CONSTRAINT pk_lifecycle_actions PRIMARY KEY (id)
);

CREATE TABLE LIFECYCLE_APPLICATION_STATES
(
    state       SMALLINT,
    eventId     UUID         NOT NULL,
    application VARCHAR(255) NOT NULL,
    CONSTRAINT pk_lifecycle_application_states PRIMARY KEY (eventId, application)
);

ALTER TABLE LIFECYCLE_APPLICATION_STATES
    ADD CONSTRAINT eventAndAppConstraint UNIQUE (eventId, application);

ALTER TABLE DISTRIBUTION_STATES
    ADD CONSTRAINT uc_distribution_states_distributionid UNIQUE (distributionId);

ALTER TABLE LIFECYCLE_ACTIONS
    ADD CONSTRAINT uc_lifecycle_actions_eventid UNIQUE (eventId);
