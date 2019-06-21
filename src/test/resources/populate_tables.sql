DROP TABLE IF EXISTS issue_metrics_table;
CREATE TABLE IF NOT EXISTS issue_metrics_table (
    domain TEXT NOT NULL,
    issue_id TEXT NOT NULL,
    created_at BIGINT NOT NULL,
    inbound_count BIGINT,
    outbound_count BIGINT,
    csat INT
);

INSERT INTO issue_metrics_table VALUES('hscustomer','1',1000,100,50, 4);
INSERT INTO issue_metrics_table VALUES('hscustomer','2',1001,100,50, 1);
INSERT INTO issue_metrics_table VALUES('hscustomer','3',1002,200,150, 3);
INSERT INTO issue_metrics_table VALUES('hscustomer','4',1003,200,150, 3);
INSERT INTO issue_metrics_table VALUES('abc','5',1004,200,150, 3);

DROP TABLE IF EXISTS agent_metrics_table;
CREATE TABLE IF NOT EXISTS agent_metrics_table (
    domain TEXT NOT NULL,
    time_bucket TEXT NOT NULL,
    agent_id TEXT NOT NULL,
    ttfr_sum BIGINT,
    ttfr_count BIGINT,
    csat INT
);

INSERT INTO agent_metrics_table VALUES('hscustomer','201901010130',"a1",100,50, 4);
INSERT INTO agent_metrics_table VALUES('hscustomer','201901010200',"a1",200,150, 3);
INSERT INTO agent_metrics_table VALUES('hscustomer','201901010230',"a1",200,150, 3);
INSERT INTO agent_metrics_table VALUES('hscustomer','201901010130',"a2",200,150, 3);
INSERT INTO agent_metrics_table VALUES('hscustomer','201901010200',"a2",200,150, 5);
INSERT INTO agent_metrics_table VALUES('abc','201901010230',"aa2",200,150, 3);