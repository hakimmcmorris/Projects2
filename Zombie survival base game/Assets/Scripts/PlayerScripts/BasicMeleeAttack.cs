using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BasicMeleeAttack : MonoBehaviour
{
    public Transform attackPosition;
    public float attackRange;
    public LayerMask enemyMask;
    public int damage;
    //Make sure not to set it too low. A good number is 5000
    public int knockBackEffectStrength;

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            // Collects the colliders that are in the AOE of the melee attack
            // Only recognizes GameObjects in the 'Enemy' Layer
            Collider2D[] enemiesInRange = Physics2D.OverlapCircleAll(attackPosition.position, attackRange, enemyMask);

            // Runs through each of the enemies in range and deals them damage
            for (int i = 0; i < enemiesInRange.Length; i++)
            {
                enemiesInRange[i].GetComponent<Enemy1>().TakeDamage(damage);
                enemiesInRange[i].GetComponent<Enemy1>().SetEffects(true, 0.2f, "knockback");
                KnockBack(enemiesInRange[i].GetComponent<Rigidbody2D>());
            }
        }
    }

    // Knocks the enemy back in the opposite direction its facing the player
    void KnockBack(Rigidbody2D enemy)
    {
        Vector3 direction = enemy.transform.position - transform.position;
        enemy.AddForce(direction * knockBackEffectStrength, ForceMode2D.Impulse);
    }

    // Visual for the area of effect for the melee attack
    private void OnDrawGizmos()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(attackPosition.position, attackRange);
    }
}
