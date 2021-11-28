using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AreaTrigger : MonoBehaviour
{
    [SerializeField] GameObject[] SpawnManagers;
    [SerializeField] GameObject RemoveableObject;
    [SerializeField] GameObject Teleporter = null;
    float timer = 0.5f;

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.CompareTag("Player"))
        {
            foreach (GameObject s in SpawnManagers)
            {
                s.SetActive(true);
            }

            RemoveableObject.SetActive(true);
        }
    }

    private void Update()
    {
        for (int i = 0; i < SpawnManagers.Length; i++)
        {
            if (SpawnManagers[i].activeInHierarchy)
            {
                timer -= Time.deltaTime;
                if (timer <= 0 && SpawnManagers[i].GetComponent<WaveManager>().state == WaveManager.SpawnState.COUNTING)
                {
                    SpawnManagers[i].SetActive(false);
                }

                if (timer <= 0 && !SpawnManagers[SpawnManagers.Length - 1].activeInHierarchy)
                {
                    RemoveableObject.SetActive(false);
                    if (Teleporter)
                    {
                        Teleporter.SetActive(true);
                    }
                    this.gameObject.SetActive(false);
                }
            }
        }

    }
}
