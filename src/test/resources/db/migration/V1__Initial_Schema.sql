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

create table if not exists managers_t
(
	manager_id bigint,
	team_id bigint,
	player_id bigint,
	year char(4),
	first_name varchar(30),
	last_name varchar(30),
	age bigint,
	offense double precision,
	defense double precision,
	intangible double precision,
	penalties double precision,
	vitality double precision,
	style bigint,
	new_hire bigint,
	released bigint,
	retired bigint,
	former_team_id bigint,
	allstar_team_id bigint,
	award bigint,
	seasons bigint,
	score bigint,
	total_seasons bigint,
	total_score bigint
);

create index if not exists idx_16404_manager_team_id
	on managers_t (team_id);

create index if not exists idx_16404_manager_manager_id
	on managers_t (manager_id);

create index if not exists idx_16404_manager_year
	on managers_t (year);

create index if not exists idx_16404_manager_player_id
	on managers_t (player_id);

create table if not exists players_t
(
    player_id bigint,
    team_id bigint,
    year char(4),
    first_name varchar(30),
    last_name varchar(30),
    age bigint,
    scoring double precision,
    passing double precision,
    blocking double precision,
    tackling double precision,
    stealing double precision,
    presence double precision,
    discipline double precision,
    penalty_shot double precision,
    penalty_offense double precision,
    penalty_defense double precision,
    endurance double precision,
    confidence double precision,
    vitality double precision,
    durability double precision,
    rookie bigint,
    injured bigint,
    return_date date,
    free_agent bigint,
    signed bigint,
    released bigint,
    retired bigint,
    former_team_id bigint,
    allstar_team_id bigint,
    award bigint,
    draft_pick bigint,
    seasons_played bigint,
    allstar_alternate bigint
);

create index if not exists idx_16410_player_year
    on players_t (year);

create index if not exists idx_16410_player_player_id
    on players_t (player_id);

create index if not exists idx_16410_player_team_id
    on players_t (team_id);

create table if not exists team_offense_sum_t
(
    year char(4),
    type bigint,
    team_id bigint,
    games bigint,
    possessions bigint,
    possession_time bigint,
    attempts bigint,
    goals bigint,
    turnovers bigint,
    steals bigint,
    penalties bigint,
    offensive_penalties bigint,
    psa bigint,
    psm bigint,
    ot_psa bigint,
    ot_psm bigint,
    score bigint
);

create index if not exists idx_16428_team_offense_team_id
    on team_offense_sum_t (team_id);

create index if not exists idx_16428_team_offense_year
    on team_offense_sum_t (year);

