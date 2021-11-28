using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Shotgun : Weapons 
{
    public override void Attack(Vector3 position, Vector2 looking)
    {
        Debug.Log("eat shit");
        //last_fired = Time.time + fire_rate;
        Bullet clone = Instantiate(BulletType, position, Quaternion.identity);
        clone.damage = damage;
        clone.GetComponent<Rigidbody2D>().velocity = looking.normalized * bullet_speed;
        Bullet clone2 = Instantiate(BulletType, position, Quaternion.identity);
        clone2.damage = damage;
        clone2.GetComponent<Rigidbody2D>().velocity = Rotate(looking.normalized, .1f) * bullet_speed;
        Bullet clone3 = Instantiate(BulletType, position, Quaternion.identity);
        clone3.damage = damage;
        clone3.GetComponent<Rigidbody2D>().velocity = Rotate(looking.normalized, -.1f) * bullet_speed;
    }
}
