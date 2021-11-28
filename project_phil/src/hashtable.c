#include "hashtable.h"

/*
 * General utility routines (including malloc()).
 */
#include <stdlib.h>

/*
 * Standard IO and file routines.
 */
#include <stdio.h>

/*
 * String utility routines.
 */
#include <string.h>

/*
 * This creates a new hash table of the specified size and with
 * the given hash function and comparison function.
 */
HashTable *createHashTable(int size, unsigned int (*hashFunction)(void *),
                           int (*equalFunction)(void *, void *)) {
  int i = 0;
  HashTable *newTable = malloc(sizeof(HashTable));
  if (NULL == newTable) {
    fprintf(stderr, "malloc failed \n");
    exit(1);
  }
  newTable->size = size;
  newTable->buckets = malloc(sizeof(struct HashBucketEntry *) * size);
  if (NULL == newTable->buckets) {
    fprintf(stderr, "malloc failed \n");
    exit(1);
  }

  for (i = 0; i < size; i++) {
    newTable->buckets[i] = NULL;
  }
  newTable->hashFunction = hashFunction;
  newTable->equalFunction = equalFunction;
  return newTable;
}

/* Task 1.2 */
void insertData(HashTable *table, void *key, void *data) {
  // -- TODO --
  // HINT:
  // 1. Find the right hash bucket location with table->hashFunction.
  // 2. Allocate a new hash bucket entry struct.
  // 3. Append to the linked list or create it if it does not yet exist.
  unsigned int key_loc = table->hashFunction(key) % table->size; // location of the correct hashbucket using the key given
  struct HashBucketEntry *newHashBucket = (struct HashBucketEntry *) malloc(sizeof(struct HashBucketEntry)); // Allocated new HashBucket
  newHashBucket->key = key; // Add the information of this new HashBucket and set next equal to NULL
  newHashBucket->data = data;
  newHashBucket->next = NULL;
  if (table->buckets[key_loc] == NULL) {
    table->buckets[key_loc] = newHashBucket; // Create new bucket because it does not exist in the table
  } else {
    struct HashBucketEntry *curr = table->buckets[key_loc];
    while (curr->next != NULL) {  // Hop through the linked list
      curr = curr->next;
    }

    curr->next = newHashBucket; // Append to end of linked list of buckets
  }
}

/* Task 1.3 */
void *findData(HashTable *table, void *key) {
  // -- TODO --
  // HINT:
  // 1. Find the right hash bucket with table->hashFunction.
  // 2. Walk the linked list and check for equality with table->equalFunction.
  unsigned int key_loc = table->hashFunction(key) % table->size; // location of the correct hashbucket using the key given
  if (table->buckets[key_loc] == NULL) {
    return NULL;
  } else {
    struct HashBucketEntry *curr = table->buckets[key_loc];
    while (curr != NULL) {  // Hop through the linked list
      if (table->equalFunction(curr->key, key) == 1) { // Check to see if the key matches with the hashbuckets key
        return curr->data;  // Return a void pointer of the hashbucket
      }

      curr = curr->next;
    }

    return NULL; // Return NULL if the key doesn't match up with any of the keys stored
  }
}

/* Task 2.1 */
unsigned int stringHash(void *s) {
  // -- TODO --
  /* To suppress compiler warning until you implement this function, */
  int i;
  char *x = (char *) s;
  char *iter = x;
  int hash = 1;
  int asciiNum = 0;
  for (i = 0; i < strlen(s); i++) {
    asciiNum = *(iter);
    hash = ((hash % 31) * 3 * asciiNum) % 51;
    iter++;
  }

  if (asciiNum < 93 && asciiNum > 0) {
    hash = hash * 5; 
  }

  return hash;
}

/* Task 2.2 */
int stringEquals(void *s1, void *s2) {
  // -- TODO --
  /* To suppress compiler warning until you implement this function */
  if (strcmp((char *) s1, (char *) s2) == 0 || (strlen(s1) == 0 && strlen(s2) == 0)) {
    return 1;
  }
  return 0;
}
