using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WaveManagerOnTrigger : WaveManager
{
    // Start is called before the first frame update
    private bool triggered = false;
    // Update is called once per frame
    public void Update()
    {
        if(triggered)
        {
            if (state == SpawnState.WAITING)
            {
            if (!EnemyIsAlive())
            {
                //begin an new round
                WaveCompleted();
               

            } 
            else
            {
                return;
            }
            }
            if (Wavecountdown <= 0)
            {
                if(state != SpawnState.SPAWNING)
                {
                    //start spawning wave
                    StartCoroutine(SpawnWave(waves[nextwave]));
                }
            }
            else
            {
                Wavecountdown -= Time.deltaTime;
            }
        }
    }

    void OnTriggerEnter2D(Collider2D checktouch)
    {
        if(checktouch.CompareTag("Player") && triggered == false)
        {
            triggered = true;
            Debug.Log("im such a mess, im lost, im no good at this im in love still in love still in love with you hard as a i try i cant even drink this pain away");
            //checktouch.GetComponent<Player>().SetSpawn(transform.position);
        }
    }
}
