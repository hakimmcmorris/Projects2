using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyBullets : Bullet
{
    public override void Update()
    {
        if(Vector3.Distance(StartingPos, transform.position)> 100f)
        {
            Debug.Log(StartingPos);
            Debug.Log(transform.position);
            Destroy(this.gameObject);
        }
    }

    public override void OnTriggerEnter2D(Collider2D collision)
    {
        Debug.Log("Can't get my mind out of those memories");
        if (collision.transform.CompareTag("Player"))
        {
            Debug.Log("Still music keeps on turning me from the words that hurt my soul");
            collision.GetComponent<Player>().TakeDamage(damage);
            
        }
        piercing--;
        if(piercing <= 0)
        {
            Destroy(this.gameObject);
        }
    }
}
