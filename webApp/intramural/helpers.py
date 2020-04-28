def is_empty(fields):
    for field in fields:
        if field is None or field == '':
            return True
    return False

def create_schedule(teams_list):
    """Create a schedule for the teams in the list and return it"""
    s = []
    print(teams_list)
    if len(teams_list) % 2 == 1: teams_list = teams_list + ["BYE"]

    for i in range(len(team_list)-1):
        mid = int(len(team_list)/2)
        l1 = team_list[:mid]
        l2 = team_list[:mid]
        l2.reverse()

        # Switch sides after each round
        if(i % 2 == 1):
            s = s + [zip(l1, l2)]
        else:
            s= s + [zip(l2,l1)]

        team_list.insert(1, team_list.pop())

    return s
