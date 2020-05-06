def is_empty(fields):
    for field in fields:
        if field is None or field == '':
            return True
    return False

def generate_schedule(teams_list):
    """Create a schedule for the teams in the list and return it"""
    sched = []
    print("CS TL ::: {}".format(teams_list))
    if len(teams_list) % 2 == 1: teams_list = teams_list + [{"name" : "BYE"}]

    for i in range(len(teams_list)-1):
        mid = int(len(teams_list)/2)
        l1 = teams_list[:mid]
        l2 = teams_list[mid:]
        l2.reverse()

        # Switch sides after each round
        if(i % 2 == 1):
            sched = sched + [zip(l1, l2)]
        else:
            sched = sched + [zip(l2,l1)]

        teams_list.insert(1, teams_list.pop())

    return sched
