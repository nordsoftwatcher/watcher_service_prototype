import React from 'react';
import styles from './Divider.module.css';

interface DividerProps {
  style?: React.CSSProperties;
}

export const Divider: React.StatelessComponent<DividerProps> =
  (props) => <div {...props} className={styles.divider} />;
