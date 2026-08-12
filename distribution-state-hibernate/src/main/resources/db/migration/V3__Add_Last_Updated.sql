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

ALTER TABLE LIFECYCLE_ACTIONS
    ADD COLUMN lastUpdated TIMESTAMP NOT NULL DEFAULT now();

-- Column temporarily nullabe, Java migration V4__Populate_Distribution_IDs will populate this
-- Then SQL migration V5_Enforce_Distribution_ID_Not_Null will enforce NOT NULL constraint
ALTER TABLE LIFECYCLE_ACTIONS
    ADD COLUMN distributionID VARCHAR(500) NULL;

ALTER TABLE LIFECYCLE_APPLICATION_STATES
    ADD COLUMN lastUpdated TIMESTAMP NOT NULL DEFAULT now();
