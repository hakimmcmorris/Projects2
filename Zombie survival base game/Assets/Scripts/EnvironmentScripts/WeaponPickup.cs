using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WeaponPickup : MonoBehaviour
{
    public Weapons newWeapon;

    public void OnTriggerEnter2D(Collider2D collision) 
    {
        if(collision.transform.CompareTag("Player"))
        {
            collision.gameObject.GetComponent<Player>().CurrentWeapon = newWeapon;
            collision.gameObject.GetComponent<Player>().allWeapons.Add(newWeapon);
            collision.gameObject.GetComponent<Player>().weaponIndex = collision.gameObject.GetComponent<Player>().allWeapons.Count;
            Destroy(this.gameObject);
        }
    }
}
