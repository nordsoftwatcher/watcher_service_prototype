import React from 'react';
import styles from './Badge.module.css';

export const Badge: React.StatelessComponent = (props) => (
  <div className={styles.badge}>
    {props.children}
  </div>
)