def is_empty(fields):
    for field in fields:
        if field is None or field == '':
            return True
    return False

"""def_create_schedule(list):
    Create a schedule for the teams in the list and return it
    s = []
    total_teams = len(list)
    teams_for_bracket = 2

    while True:
        if total_teams > teams_for_bracket:
            teams_for_bracket = teams_for_bracket * 2
        else
            break;
"""
