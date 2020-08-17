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

create table if not exists team_defense_sum_t
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

create index if not exists idx_16425_team_defense_team_id
    on team_defense_sum_t (team_id);

create index if not exists idx_16425_team_defense_year
    on team_defense_sum_t (year);

create table if not exists player_stats_sum_t
(
    year char(4),
    type bigint,
    player_id bigint,
    games bigint,
    games_started bigint,
    playing_time bigint,
    attempts bigint,
    goals bigint,
    assists bigint,
    turnovers bigint,
    stops bigint,
    steals bigint,
    penalties bigint,
    offensive_penalties bigint,
    psa bigint,
    psm bigint,
    ot_psa bigint,
    ot_psm bigint,
    team_id bigint
);

create index if not exists idx_16413_player_stats_year
    on player_stats_sum_t (year);

create index if not exists idx_16413_player_stats_player_id
    on player_stats_sum_t (player_id);

create table if not exists schedule_t
(
    year char(4),
    sequence bigint,
    type bigint,
    data varchar(50),
    scheduled date,
    status bigint
);

create index if not exists idx_16416_schedule_year
    on schedule_t (year);

create index if not exists idx_16416_schedule_sequence
    on schedule_t (sequence);

create table if not exists teamgames_t
(
    game_id bigint,
    year char(4),
    datestamp date,
    type bigint,
    playoff_round bigint,
    team_id bigint,
    opponent bigint,
    road bigint,
    overtime bigint,
    win bigint,
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
    period1_score bigint,
    period2_score bigint,
    period3_score bigint,
    period4_score bigint,
    period5_score bigint,
    overtime_score bigint,
    total_score bigint
);

create index if not exists idx_16419_teamgames_game_id
    on teamgames_t (game_id);

create index if not exists idx_16419_teamgames_datestamp
    on teamgames_t (datestamp);

create index if not exists idx_16419_teamgames_year
    on teamgames_t (year);

create table if not exists playergames_t
(
    game_id bigint,
    year char(4),
    datestamp date,
    type bigint,
    player_id bigint,
    team_id bigint,
    injured bigint,
    started bigint,
    playing_time bigint,
    attempts bigint,
    goals bigint,
    assists bigint,
    turnovers bigint,
    stops bigint,
    steals bigint,
    penalties bigint,
    offensive_penalties bigint,
    psa bigint,
    psm bigint,
    ot_psa bigint,
    ot_psm bigint,
    offense bigint,
    points bigint
);

create index if not exists idx_16407_playergames_penalties
    on playergames_t (penalties);

create index if not exists idx_16407_playergames_turnovers
    on playergames_t (turnovers);

create index if not exists idx_16407_playergames_points
    on playergames_t (points);

create index if not exists idx_16407_playergames_stops
    on playergames_t (stops);

create index if not exists idx_16407_playergames_psa
    on playergames_t (psa);

create index if not exists idx_16407_playergames_psm
    on playergames_t (psm);

create index if not exists idx_16407_playergames_datestamp
    on playergames_t (datestamp);

create index if not exists idx_16407_playergames_goals
    on playergames_t (goals);

create index if not exists idx_16407_playergames_player_id
    on playergames_t (player_id);

create index if not exists idx_16407_playergames_ot_psa
    on playergames_t (ot_psa);

create index if not exists idx_16407_playergames_year
    on playergames_t (year);

create index if not exists idx_16407_playergames_team_id
    on playergames_t (team_id);

create index if not exists idx_16407_playergames_playing_time
    on playergames_t (playing_time);

create index if not exists idx_16407_playergames_game_id
    on playergames_t (game_id);

create index if not exists idx_16407_playergames_ot_psm
    on playergames_t (ot_psm);

create index if not exists idx_16407_playergames_attempts
    on playergames_t (attempts);

create index if not exists idx_16407_playergames_steals
    on playergames_t (steals);

create index if not exists idx_16407_playergames_offense
    on playergames_t (offense);

create index if not exists idx_16407_playergames_assists
    on playergames_t (assists);

create table if not exists injuries_t
(
    game_id bigint,
    player_id bigint,
    team_id bigint,
    duration bigint
);

create index if not exists idx_16398_injury_player_id
    on injuries_t (player_id);

create index if not exists idx_16398_injury_team_id
    on injuries_t (team_id);

create index if not exists idx_16398_injury_game_id
    on injuries_t (game_id);

create table if not exists gamestate_t
(
    game_id bigint,
    started bigint,
    start_time bigint,
    sequence bigint,
    period bigint,
    overtime bigint,
    time_remaining bigint,
    clock_stopped bigint,
    possession bigint,
    last_event varchar(200)
);

create index if not exists idx_16395_gamestate_game_id
    on gamestate_t (game_id);

