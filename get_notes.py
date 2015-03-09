# Mitch Vollebregt, March 9 2015 
# Queen's computing note grabber
import urllib
import urllib2
from bs4 import BeautifulSoup

# Dictionary of supported courses and their index url
course = {'cisc235': 'http://sites.cs.queensu.ca/courses/cisc235/', 
          'cisc223': 'http://research.cs.queensu.ca/home/cisc223/2013w/mon.html'}

# Get desired course and destination path from user
# then pass values to get notes
def get_input():
  selected_course = raw_input("Enter a course: (cisc235, cisc223): ")
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
  if (selected_course == "cisc235"):
    search_token = 'Record/'
    course_index_page = course['cisc235']
  elif (selected_course == "cisc223"):
    search_token = 'moni'
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