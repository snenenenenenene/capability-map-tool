import React, { useState } from 'react';
import classNames from 'clsx';
import {
    TreeDataState,
    CustomTreeData,
    EditingState
  } from '@devexpress/dx-react-grid';
  import {
    Grid,
    Table,
    TableHeaderRow,
    TableTreeColumn,
    TableEditRow,
    TableEditColumn,
  } from '@devexpress/dx-react-grid-bootstrap4';
  import '@devexpress/dx-react-grid-bootstrap4/dist/dx-react-grid-bootstrap4.css';
  
const getChildRows = (row, rootRows) => {
    const childRows = rootRows.filter(
      (r) => r.parentId === (row ? row.id : null)
    );
    return childRows.length ? childRows : null;
  };

  const TableComponent = ({ ...restProps }) => (
    <Table.Table
      {...restProps}
      className="table-striped"
    />
  );

export default (props) => {
  const [columns] = useState([
    { name: 'id', title: 'ID' },
    { name: 'name', title: 'Name' },
    { name: 'level', title: 'Level' },
  ]);
  const [tableColumnExtensions] = useState([
    { columnName: 'name', width: 300,},
  ]);

  const commitChanges = ({ added, changed, deleted }) => {
    if (deleted) {
        console.log("deleted")
    }
  };

  return (
      <Grid
        rows={props.capabilities}
        columns={columns}
      >
        <TreeDataState />
        <CustomTreeData getChildRows={getChildRows}/>
        <Table columnExtensions={tableColumnExtensions} tableComponent={TableComponent}/>
        <EditingState
          onCommitChanges={commitChanges}
        />
        <TableHeaderRow />
        <TableEditRow />
        <TableEditColumn
          showDeleteCommand
        />
        <TableTreeColumn for="id"/>
      </Grid>
  );
};
