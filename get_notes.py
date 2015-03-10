# Mitch Vollebregt, March 9 2015 
# Queen's computing note grabber
import urllib
import urllib2
from bs4 import BeautifulSoup

# Dictionary of supported courses and their index url
course = {'cisc235': 'http://sites.cs.queensu.ca/courses/cisc235/', 
          'cisc223': 'http://research.cs.queensu.ca/home/cisc223/2013w/mon.html',
          'cisc260': 'http://research.cs.queensu.ca/home/cisc260/2015w/schedule.html'}

# Search through links passed as a parameter and find the token that occurs
# most frequently. The idea is that this token will be used in the naming
# scheme for the notes. We throw out tokens that are comman for webpages
def find_token(all_links):
  words = {}
  non_acceptable = [' ', '', 'http', 'http:', 'www.cs.queensu.ca:80']
  most_frequent = ''
  most_frequent_count = -1

  for link in all_links:
    if (link.get('href')):
      link = link.get('href')
      for token in link.split('/'):
        
        if (token in words):
          words[token] += 1
          if (words[token] > most_frequent_count):
            most_frequent = token
            most_frequent_count = words[token]

        elif (token not in non_acceptable):
          words[token] = 1
  print ("token used: "+most_frequent)
  return most_frequent

# List available course in the form [course1, course2, course3]
def list_courses():
  lis = []
  for i in course:
    lis.append(i)
  return ", ".join(lis)

# Get desired course and destination path from user
# then pass values to get notes
def get_input():
  selected_course = raw_input("Enter a course: "+list_courses()+": ")
  selected_folder = raw_input("Enter a destination folder (absolute path): ")
  
  if (selected_folder[-1] != '/'):
    selected_folder += '/'
  
  get_notes(selected_course, selected_folder)

def get_notes(selected_course, selected_folder):
  # Get index page for selected course
  page = urllib2.urlopen(course[selected_course]).read()
  soup = BeautifulSoup(page)
  soup.prettify()

  # Adjust search tokens and proper index pages for each course
  search_token = find_token(soup.find_all('a'))
  if (selected_course == "cisc235"):
    # The course_index_page is the same as the main course page
    course_index_page = course['cisc235'] 
  elif (selected_course == "cisc223"):
    # The course_index_page containing notes is different from the main page
    course_index_page = 'http://research.cs.queensu.ca/home/cisc223/2013w/'

  # Loop through all links on index page and 
  # link for a search_token to verify link is for a note.
  # 
  for link in soup.find_all('a'):
    if (link.get('href') != None):
      if (search_token in link.get('href')):
        # server_file_name is the relative path from the course_index_page
        # whereas local_file_name is just the file name
        server_file_name = link.get('href')
        local_file_name = server_file_name.split('/')[-1]

        # server_location is the full path of the note on the course server
        server_location = course_index_page
        server_location += server_file_name

        # local_location is the full path for the local file
        local_location = selected_folder
        local_location += local_file_name

        # Download file from server and save to specified location
        print('downloading: \"'+local_file_name+'\" -from- \"'+server_location+'\"')
        urllib.urlretrieve(server_location, local_location)

get_input()