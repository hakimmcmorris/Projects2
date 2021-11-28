/*
 * Include the provided hash table library.
 */
#include "hashtable.h"

/*
 * Include the header file.
 */
#include "philphix.h"

/*
 * Standard IO and file routines.
 */
#include <stdio.h>

/*
 * General utility routines (including malloc()).
 */
#include <stdlib.h>

/*
 * Character utility routines.
 */
#include <ctype.h>

/*
 * String utility routines.
 */
#include <string.h>

/*
 * This hash table stores the dictionary.
 */
HashTable *dictionary;

/*
 * The MAIN routine.  You can safely print debugging information
 * to standard error (stderr) as shown and it will be ignored in 
 * the grading process.
 */
#ifndef _PHILPHIX_UNITTEST
int main(int argc, char **argv) {
  if (argc != 2) {
    fprintf(stderr, "Specify a dictionary\n");
    return 1;
  }
  /*
   * Allocate a hash table to store the dictionary.
   */
  fprintf(stderr, "Creating hashtable\n");
  dictionary = createHashTable(0x61C, &stringHash, &stringEquals);

  fprintf(stderr, "Loading dictionary %s\n", argv[1]);
  readDictionary(argv[1]);
  fprintf(stderr, "Dictionary loaded\n");

  fprintf(stderr, "Processing stdin\n");
  processInput();

  /*
   * The MAIN function in C should always return 0 as a way of telling
   * whatever program invoked this that everything went OK.
   */
  return 0;
}
#endif /* _PHILPHIX_UNITTEST */

/* Task 3 */
void readDictionary(char *dictName) {
  // -- TODO --
  FILE *openedDict = fopen(dictName, "r");
  if (openedDict == NULL) {
    fprintf(stderr, "Dictionary was not able to be opened\n");
    exit(61);
  }

  char *key_1 = (char *) malloc(1 * sizeof(char));
  int lenKey = 0;
  char *data_1 = (char *) malloc(1 * sizeof(char));
  int lenData = 0;
  memset(key_1,0,1);
  memset(data_1,0,1);
  //fprintf(stderr, "Key: %s\n", key_1);
  char buff[2] = "\0";
  buff[0] = fgetc(openedDict);
  //fprintf(stderr, "%c\n", c);
  int keyDataIndicator = 0;
  while(buff[0] != EOF) {
    fprintf(stderr, "%c\n", buff[0]);
    if (isalnum(buff[0]) && keyDataIndicator % 2 == 0) {
      if (lenKey == 0) {
        key_1 = realloc(key_1, 2 * sizeof(char));
	strncat(key_1, buff, 1);
	lenKey++;
	//fprintf(stderr, "%s\n", key_1);
      } else {
	//printf("Original String: %s\n", key_1);
        //printf("Character to be appended: %c\n", c);
        key_1 = realloc(key_1, (lenKey + 2) * sizeof(char));
	strncat(key_1, buff, 1);
	lenKey++;
	//fprintf(stderr, "%s\n", key_1);
      }
    }


    if (keyDataIndicator % 2 == 1 && buff[0] != 9 && buff[0] != 32 && buff[0] != 10) {
      if (lenData == 0) {
        data_1 = realloc(data_1, 2 * sizeof(char));
        strncat(data_1, buff, 1);
	lenData++;
	//fprintf(stderr, "%s\n", data_1);
      } else {
	//printf("Original String: %s\n", data_1);
        //printf("Character to be appended: %c\n", c);
        data_1 = realloc(data_1, (lenData + 2) * sizeof(char));
        strncat(data_1, buff, 1);
	lenData++;
	//fprintf(stderr, "%s\n", data_1);
      }
    }

    buff[0] = fgetc(openedDict);

    if (buff[0] == 9 || buff[0] == 32) {
      keyDataIndicator = 1;
    }

    if (buff[0] == 10 || buff[0] == EOF) {
      keyDataIndicator = 0;
      insertData(dictionary, (void *) key_1, (void *) data_1);
      key_1 = NULL;
      data_1 = NULL;
      key_1 = (char *) malloc(1 * sizeof(char));
      data_1 = (char *) malloc(1 * sizeof(char));
      memset(key_1,0,1);
      memset(data_1,0,1);
      lenKey = 0;
      lenData = 0;
    }
  }

  free(key_1);
  free(data_1);
  fclose(openedDict);

}

/* Task 4 */
void processInput() {
  // -- TODO --
  fprintf(stderr, "You need to implement processInput\n");

  char *keyHolder = NULL;

  char *foundData;
  char *allExceptFirstLower;
  char *allLower;
  char *iter_1;
  char *iter_2;
  int i;
  char buff[2] = "\0";

  buff[0] = fgetc(stdin);
  fprintf(stderr, "%c\n", buff[0]);
  while (feof(stdin) == 0) {
    fprintf(stderr, "%c\n", buff[0]);

    if (keyHolder == NULL) {
      keyHolder = (char *) malloc(2 * sizeof(char));
      memset(keyHolder, 0, 2);
    }

    if (isalnum(buff[0])) {

      if (keyHolder == NULL) {
        keyHolder = (char *) malloc(2 * sizeof(char));
      } else {
        keyHolder = realloc(keyHolder, (strlen(keyHolder) + 2) * sizeof(char));
      }

      strncat(keyHolder, buff, 1);

    } else if (strlen(keyHolder) > 0) {

      foundData = (char *) findData(dictionary, (void *) keyHolder);

      if (foundData == NULL) {
        allExceptFirstLower = (char *) malloc((strlen(keyHolder) + 1) * sizeof(char));
	allLower = (char *) malloc((strlen(keyHolder) + 1) * sizeof(char));
	memset(allExceptFirstLower,0,strlen(keyHolder) + 1);
	memset(allLower,0,strlen(keyHolder) + 1);
	strcpy(allExceptFirstLower, keyHolder);
	strcpy(allLower, keyHolder);
	iter_1 = allExceptFirstLower;
	iter_2 = allLower;

	for (i = 0; i < strlen(allExceptFirstLower); i++) {
	
	  if (i != 0) {
	    *(iter_1) = tolower(*(iter_1));
	  }
	  *(iter_2) = tolower(*(iter_2));
	  iter_1++;
	  iter_2++;
	  
	}

	fprintf(stderr, "allExceptFirstLower = %s\n", allExceptFirstLower);
	fprintf(stderr, "allLower = %s\n", allLower);

	foundData = (char *) findData(dictionary, (void *) allExceptFirstLower);

	if (foundData != NULL) {
	
	  fprintf(stdout, "%s", foundData);
	  fprintf(stdout, "%c", buff[0]);
	  free(allExceptFirstLower);
          free(allLower);
          allExceptFirstLower = NULL;
          allLower = NULL;
          free(keyHolder);
          keyHolder = NULL;

	} else if (allLower != NULL) {
	
	  foundData = (char *) findData(dictionary, (void *) allLower);

	  if (foundData != NULL) {
	
	    fprintf(stdout, "%s", foundData);
	    fprintf(stdout, "%c", buff[0]);
	    free(allExceptFirstLower);
            free(allLower);
            allExceptFirstLower = NULL;
            allLower = NULL;
	    free(keyHolder);
            keyHolder = NULL;

	  } else {

	    free(allExceptFirstLower);
            free(allLower);
            allExceptFirstLower = NULL;
            allLower = NULL;
            fprintf(stdout, "%s", keyHolder);
            fprintf(stdout, "%c", buff[0]);
            free(keyHolder);
            keyHolder = NULL;

	  }
	} else {

	  free(allExceptFirstLower);
          free(allLower);
          allExceptFirstLower = NULL;
          allLower = NULL;
          fprintf(stdout, "%s", keyHolder);
          fprintf(stdout, "%c", buff[0]);
          free(keyHolder);
          keyHolder = NULL;

	}

      } else {
	
	fprintf(stdout, "%s", foundData);
	fprintf(stdout, "%c", buff[0]);
	memset(keyHolder,0,strlen(keyHolder));
	free(keyHolder);
	keyHolder = NULL;
	
      }
    } else {
      fprintf(stdout, "%c", buff[0]);
      memset(keyHolder,0,strlen(keyHolder));
      free(keyHolder);
      keyHolder = NULL;
    }
    
    
    buff[0] = fgetc(stdin);
  }

  if (keyHolder != NULL) {
    foundData = (char *) findData(dictionary, (void *) keyHolder);
    if (foundData == NULL) {
      allExceptFirstLower = (char *) malloc((strlen(keyHolder) + 1) * sizeof(char));
      allLower = (char *) malloc((strlen(keyHolder) + 1) * sizeof(char));
      memset(allExceptFirstLower,0,strlen(allExceptFirstLower));
      memset(allLower,0,strlen(allLower));
      strcpy(allExceptFirstLower, keyHolder);
      strcpy(allLower, keyHolder);
      iter_1 = allExceptFirstLower;
      iter_2 = allLower;
      for (i = 0; i < strlen(allExceptFirstLower); i++) {
        if (i != 0) {
          *(iter_1) = tolower(*(iter_1));
        }
        *(iter_2) = tolower(*(iter_2));
        iter_1++;
        iter_2++;
      }

      foundData = (char *) findData(dictionary, (void *) allExceptFirstLower);
      if (foundData != NULL) {
        fprintf(stdout, "%s", foundData);
        free(allExceptFirstLower);
        free(allLower);
        allExceptFirstLower = NULL;
        allLower = NULL;
        free(keyHolder);
        keyHolder = NULL;
      } else if (allLower != NULL) {
	foundData = (char *) findData(dictionary, (void *) allLower);
        if (foundData != NULL) {
          fprintf(stdout, "%s", foundData);
          free(allExceptFirstLower);
          free(allLower);
          allExceptFirstLower = NULL;
          allLower = NULL;
          free(keyHolder);
          keyHolder = NULL;
        } else {
          free(allExceptFirstLower);
          free(allLower);
          allExceptFirstLower = NULL;
          allLower = NULL;
          fprintf(stdout, "%s", keyHolder);
          free(keyHolder);
          keyHolder = NULL;
        }
    }
  } else {
      fprintf(stdout, "%s", foundData);
      free(keyHolder);
      keyHolder = NULL;
    }
 }

  struct HashBucketEntry *curr_head;
  struct HashBucketEntry *bucketJumper;
  for (i = 0; i < dictionary->size; i++) {
    curr_head = dictionary->buckets[i];
    bucketJumper = curr_head;
    while (bucketJumper != NULL) {
      bucketJumper = curr_head;
      if (bucketJumper->next == NULL) {
        free(bucketJumper->key);
	free(bucketJumper->data);
	free(bucketJumper);
	break;
      } else {
        while (bucketJumper->next != NULL) {
	  if (bucketJumper->next->next == NULL) {
	    free(bucketJumper->next->key);
	    free(bucketJumper->next->data);
	    free(bucketJumper->next);
	    bucketJumper->next = NULL;
	  } else {
	    bucketJumper = bucketJumper->next;
	  }
	}
      }
    }

  }

  free(dictionary->buckets);
  free(dictionary);

}
