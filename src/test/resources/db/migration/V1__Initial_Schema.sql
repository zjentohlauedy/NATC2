create table if not exists teams_t
(
    team_id bigint,
    year char(4),
    location varchar(30),
    name varchar(30),
    abbrev varchar(5),
    time_zone varchar(20),
    game_time bigint,
    conference bigint,
    division bigint,
    allstar_team bigint,
    preseason_games bigint,
    preseason_wins bigint,
    preseason_losses bigint,
    games bigint,
    wins bigint,
    losses bigint,
    div_wins bigint,
    div_losses bigint,
    ooc_wins bigint,
    ooc_losses bigint,
    ot_wins bigint,
    ot_losses bigint,
    road_wins bigint,
    road_losses bigint,
    home_wins bigint,
    home_losses bigint,
    division_rank bigint,
    playoff_rank bigint,
    playoff_games bigint,
    round1_wins bigint,
    round2_wins bigint,
    round3_wins bigint,
    expectation double precision,
    drought bigint
);

create index if not exists idx_16422_team_year
    on teams_t (year);

create index if not exists idx_16422_team_team_id
    on teams_t (team_id);

