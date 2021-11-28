using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Checkpoint : MonoBehaviour
{
    private bool visited;
    private void Start()
    {
        visited = false;
    }
    void OnTriggerEnter2D(Collider2D checktouch)
    {
        if(checktouch.CompareTag("Player"))
        {
            visited = true;
            Debug.Log("neanderthal status");
            checktouch.GetComponent<Player>().SetSpawn(transform.position);
        }
    }
}
