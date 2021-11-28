using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SelfDestructWall : MonoBehaviour
{
    public GameObject wall;
    public float wallTimer;
    public bool wallExists;
    void Start()
    {
        wallExists = true;
    }

    private IEnumerator destroyWall()
    {
        yield return new WaitForSeconds(wallTimer);
        Destroy(wall);
    }

    public void OnTriggerEnter2D(Collider2D collision) 
    {
        if(collision.transform.CompareTag("Player") && wallExists)
        {
            wallExists = false;
            StartCoroutine(destroyWall());
        }
    }
}
