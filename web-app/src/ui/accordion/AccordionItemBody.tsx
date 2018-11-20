import React from 'react';
import styles from './AccordionItemBody.module.css'
import { AccordionItemContext } from './AccordionItem';

export interface AccordionBodyRenderProps {
  isOpen: boolean;
}

export interface AccordionBodyProps {
  children(props: AccordionBodyRenderProps): JSX.Element;
}

export default class AccordionItemBody extends React.Component<AccordionBodyProps> {
  render() {
    return (
      <AccordionItemContext.Consumer>
        {({ isOpen }) => (
          <div className={styles.body}>
            {this.props.children!({ isOpen })}
          </div>
        )}
      </AccordionItemContext.Consumer>
    )
  }
}