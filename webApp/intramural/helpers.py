def is_empty(fields):
    for field in fields:
        if field is None or field == '':
            return True
    return False
