# Mitch Vollebregt, March 9 2015 
# Queen's computing note grabber
from __future__ import print_function
import urllib
import urllib2
import time
import sys

from bs4 import BeautifulSoup

print("| Running Program...")

# Dictionary of supported courses and their index url
course = {'cisc235': 'http://sites.cs.queensu.ca/courses/cisc235/', 
          'cisc223': 'http://research.cs.queensu.ca/home/cisc223/2013w/mon.html',
          'cisc260': 'http://research.cs.queensu.ca/home/cisc260/2015w/schedule.html',
          'cisc223-assignments': 'http://research.cs.queensu.ca/home/cisc223/2013w/asn.html'}

# Creates a download bar for status on notes download
def download_bar(current, total):
  print("| Downloading: ", end='')
  for i in range(0, total):
    if (i <= current): print("|", end='')
    else: print("-", end='')
  total = total -1l
  print(" %"+str(round((float(current)/(total))*100.0, 1)), end='')
  sys.stdout.write('\r')
  sys.stdout.flush()

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

def download_notes(download_links):
  # Download file from server and save to specified location
  status = 0
  for i in download_links:
    local_location = i
    server_location = download_links[i]
    urllib.urlretrieve(server_location, local_location)

    # print('downloading: \"'+local_location+'\" -from- \"'+server_location+'\"')
    print(server_location)
    download_bar(status, len(download_links))
    status += 1
  

# Get desired course and destination path from user
# then get note links from webpage
def get_note_links():
  selected_course = raw_input("Enter a course: "+list_courses()+": ")
  selected_folder = raw_input("Enter a destination folder (absolute path): ")
  
  if (selected_folder[-1] != '/'):
    selected_folder += '/'
  

  # Get index page for selected course
  page = urllib2.urlopen(course[selected_course]).read()
  soup = BeautifulSoup(page)
  soup.prettify()

  download_links = {}

  # Adjust search tokens and proper index pages for each course
  search_token = find_token(soup.find_all('a'))
  if (selected_course == "cisc235"):
    # The course_index_page is the same as the main course page
    course_index_page = course['cisc235'] 
  elif (selected_course == "cisc223"):
    # The course_index_page containing notes is different from the main page
    course_index_page = 'http://research.cs.queensu.ca/home/cisc223/2013w/'
  else:
    course_index_page = course[selected_course]
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

        download_links[local_location] = server_location
  return download_links

def main():
  links = get_note_links()
  download_notes(links)

main()

print("\n| Program Finished")

