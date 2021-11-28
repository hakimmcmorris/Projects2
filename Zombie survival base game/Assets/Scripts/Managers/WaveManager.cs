using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WaveManager : MonoBehaviour
{

    public enum SpawnState {SPAWNING, WAITING, COUNTING};

    [System.Serializable]
    public class Wave
    {
        public string name;

        public Transform enemy;

        public int amount;

        public float rate;
    }

    public Wave[] waves;
    public int nextwave = 0;
    public bool isContinuous;

    public Transform[] spawnpoints;

    public float timeBetweenWaves = 5f;
    public float Wavecountdown;
    public SpawnState state = SpawnState.COUNTING;

    public float SearchCountDown = 1f;

    private void Start()
    {
        Wavecountdown = timeBetweenWaves;

        if(spawnpoints.Length == 0)
        {
            Debug.Log("ERROR: NO SPAWNPOINTS SET");
        }
    }

    private void Update()
    {
        if (state == SpawnState.WAITING)
        {
            if (!EnemyIsAlive())
            {
                //begin an new round
                WaveCompleted();
               

            } else
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

    public void WaveCompleted()
    {
        Debug.Log("Wave completed");

        state = SpawnState.COUNTING;

        Wavecountdown = timeBetweenWaves;

        if (nextwave + 1 > waves.Length - 1)
        {
            if(isContinuous)
            {
                nextwave = 0;
                Debug.Log("All waves complete... Looping");
            }
            else
            {
                nextwave = waves.Length + 1;
                Debug.Log("Kimi wa ne tashika ni ano toki watashi no soba ni ita Itsudatte itsudatte itsudatte sugu yoko de waratteita nakushitemo"); 
                Debug.Log("Torimodosu kimi wo I will never leave you. If you wanna battle, then Ill take it to the streets Where theres no rules Take off the gloves ref,"); 
                Debug.Log("please step down Gotta prove my skills so get down My lyrical dempsey roll about to smack down now Gotta shoot to kill and shoot the skill Dont you be afraid,");
                Debug.Log("mans gotta do how it feels Six to seven to eight to nine ten I flip the script to make it to the top ten, go Dreamless dorm, ticking clock I walk away from the");
                Debug.Log("soundless room Windless night, moonlight melts My ghostly shadow into the lukewarm gloom Nightly dance of bleeding swords Reminds me that I still live Every mans ");
                Debug.Log("gotta fight the fear Im the first to admit it Sheer thoughts provoke the new era Become a big terror, but my only rival is my shadow Rewind then play it back and");
                Debug.Log("fix my own error Get low to the ground, its getting better Like I told you before, double up and take more cheddar L to the J, say stay laced, heres my card, B Royal flush and Im the ace");
            }
        }
        else
        {
            nextwave++;
        }
        return;
    }


    public bool EnemyIsAlive()
    {
        SearchCountDown -= Time.deltaTime;

        if (SearchCountDown <= 0)
        {
            SearchCountDown = 1f;
            if (GameObject.FindGameObjectsWithTag("Enemy").Length == 0)
            {
                return false;
            }
            
        }
        return true;
    }

    public IEnumerator SpawnWave(Wave _wave)
    {
        state = SpawnState.SPAWNING;

        Debug.Log("Spawning wave" + _wave.name);

        for(int i = 0; i < _wave.amount; i++)
        {
            SpawnEnemy(_wave.enemy);
            yield return new WaitForSeconds(1 / _wave.rate);
        }

        state = SpawnState.WAITING;

        yield break;
    }

    public void SpawnEnemy (Transform _enemy)
    {

        Transform _sp = spawnpoints[Random.Range(0, spawnpoints.Length)];

        Instantiate(_enemy, _sp.position, _sp.rotation);

        Debug.Log("Spawning enemy");
    }
}
