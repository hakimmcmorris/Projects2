using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GooBullet : Bullet
{
    private float slowing_effect;
    private float timer;
    private GameObject enemy;


    // Start is called before the first frame update
    void Start()
    {
        slowing_effect = 0.25f;
        damage = 0;
        piercing = 0;
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject)
        {
            Debug.Log("hit something");
            if (collision.gameObject.CompareTag("Enemy"))
            {
                Debug.Log("enemy hit");
                if (!collision.gameObject.GetComponent<Enemy1>().EffectsOn())
                {
                    SlowEnemy(collision);
                    Destroy(this.gameObject);
                }
            }

            if (!collision.gameObject.CompareTag("Player"))
            {
                Debug.Log("not an enemy hit");
                Destroy(this.gameObject);
            }
        }
    }

    /* Slows the enemy down and changes it's color to green to show that the
       slow effect is occuring. A timer is also set to switch the effect off
       after 2 seconds */
    private void SlowEnemy(Collider2D collision)
    {
        Debug.Log("Slowed enemy");
        enemy = collision.gameObject;
        timer = 2.0f;
        enemy.GetComponent<Enemy1>().move_speed *= slowing_effect;
        enemy.GetComponent<Enemy1>().SetEffects(true, timer);
        enemy.GetComponent<SpriteRenderer>().color = Color.green;
        Debug.Log("Time: " + timer);
    }
}
