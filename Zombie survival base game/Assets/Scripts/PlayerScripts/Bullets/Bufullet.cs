using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Bufullet : Bullet
{
    private float slowing_effect;
    private float timer;
    private GameObject enemy;


    // Start is called before the first frame update
    void Start()
    {
        slowing_effect = 0.25f;
        //damage = 1;
        piercing = 0;
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject)
        {
            if (collision.gameObject.CompareTag("Enemy"))
            {
                collision.GetComponent<Enemy1>().TakeDamage(damage);
                if (!collision.gameObject.GetComponent<Enemy1>().EffectsOn())
                {
                    //collision.GetComponent<Enemy1>().TakeDamage(damage);
                    SlowEnemy(collision);
                    Destroy(this.gameObject);
                }
            }

            if (!collision.gameObject.CompareTag("Player"))
            {
                Destroy(this.gameObject);
            }
        }
    }

    /* Slows the enemy down and changes it's color to green to show that the
       slow effect is occuring. A timer is also set to switch the effect off
       after 2 seconds */
    private void SlowEnemy(Collider2D collision)
    {
        Debug.Log("Sue Kong");
        enemy = collision.gameObject;
        timer = 2.0f;
        enemy.GetComponent<Enemy1>().move_speed *= 0;
        enemy.GetComponent<Enemy1>().SetEffects(true, timer);
        enemy.GetComponent<SpriteRenderer>().color = Color.cyan;
        Debug.Log("Time: " + timer);
    }
}