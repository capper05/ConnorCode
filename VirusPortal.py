import json, requests, sys, datetime
url = 'https://pomber.github.io/covid19/timeseries.json'
monthlengths = [31,28,31,30,31,30,31,31,30,31,30,31]
response = requests.get(url)
response.raise_for_status()
data = json.loads(response.text)
quit_val = ""
def datecalc(day_val,month_val,year_val):
    date_num = int(day_val) - 22
    if int(month_val) > 1:
        for i in range(0,int(month_val)-1):
            date_num += monthlengths[i]
    if int(month_val) > 2:
        date_num += 1
    if int(year_val) > 2020:
        date_num += 366
        date_num += 365*(year_val-2021)
    return date_num
current_year = datetime.datetime.today().year
while not (quit_val == 'quit' or quit_val == 'QUIT' or quit_val == 'Quit'):
    print("-"*30)
    while True:
        country = input("Enter Country Name: ")
        try:
            country_data = data[country]
            break
        except KeyError:
            print('USAGE ERROR: ENTER THE NAME OF A VALID COUNTRY')
    while True:
        year = input("Enter Numerical Year: ")
        month = input("Enter Numerical Month: ")
        day = input("Enter Numerical Day: ")
        datevalue = 0
        try:
            datevalue = datecalc(int(day),int(month),int(year))
            requested_data = data[country][datevalue]
            break
        except IndexError:
            if datevalue >= len(data[country]):
                print("I'm sorry. Data is unavailable for that date.")
            else:
                print("USAGE ERROR: ENTER VALID DATE AFTER 1-22-2020.")
    location = ' '.join(country[1:]) 
    print("-"*30)
    print("Date: " + str(requested_data["date"]))
    print("Country: " + country)
    print("")
    print("Total confirmed cases: " + str(requested_data["confirmed"]))
    print("Cases Increase: " + str(requested_data["confirmed"]-data[country][datevalue-1]["confirmed"]))
    print("")
    print("Total deaths: " + str(requested_data["deaths"]))
    print("Deaths Increase: " + str(requested_data["deaths"]-data[country][datevalue-1]["deaths"]))
    print("")
    print("Total recoveries: " + str(requested_data["recovered"]))
    print("Recoveries  Increase: " + str(requested_data["recovered"]-data[country][datevalue-1]["recovered"]))
    print("-"*30)
    quit_val=input("PRESS ENTER TO CONTINUE.  TO EXIT PROGRAM TYPE 'QUIT'.")
