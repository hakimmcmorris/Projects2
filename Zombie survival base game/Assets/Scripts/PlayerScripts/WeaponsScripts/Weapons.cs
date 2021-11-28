using System.Collections;
using System.Collections.Generic;
using UnityEngine;
public class Weapons : MonoBehaviour
{
    #region Inhereted_vars
    [Tooltip("Amount of health the player should have")]
    public float damage;
    [Tooltip("Fire rate")]
    public float fire_rate;
    public float last_fired = 0f;
    public Bullet BulletType;
    [SerializeField]
    [Tooltip("Holds the different types of bullets")]
    private Bullet[] bullet_types;
    // Keeps track of the bullet index 
    private int bullet_index;
    public string Name;
    public float ReloadTime;
    [Tooltip("Speed of Bullet")]
    public float bullet_speed;
    [SerializeField] public int maxAmmo;
    public int currentAmmo;

    #endregion

    #region Unity_funcs
    public void Awake()
    {
        bullet_index = 0;
        last_fired = 0f;
        currentAmmo = maxAmmo;
    }
    #endregion

    #region Weapon_funcs
    public virtual void Attack(Vector3 position, Vector2 looking)
    {
        Debug.Log("shit eat");
        //last_fired = Time.time + fire_rate;
        Bullet clone = Instantiate(BulletType, position, Quaternion.identity);
        clone.damage = damage;
        clone.GetComponent<Rigidbody2D>().velocity = looking.normalized * bullet_speed;
    }

    public Vector2 Rotate(Vector2 v, float delta)
    {
        return new Vector2(v.x * Mathf.Cos(delta) - v.y * Mathf.Sin(delta), v.x * Mathf.Sin(delta) + v.y * Mathf.Cos(delta));
    }
    #endregion

    #region Player_funcs
    public void SwitchBullet()
    {
        bullet_index++;
        if (bullet_index >= bullet_types.Length)
        {
            bullet_index = 0;
        }
        BulletType = bullet_types[bullet_index];
        Debug.Log(BulletType.name);
    }

    #endregion
}


