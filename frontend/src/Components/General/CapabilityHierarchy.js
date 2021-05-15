import React, { useState } from 'react';
import Paper from '@material-ui/core/Paper';
import {
  TreeDataState,
  CustomTreeData,
} from '@devexpress/dx-react-grid';
import {
  Grid,
  Table,
  TableHeaderRow,
  TableTreeColumn,
} from '@devexpress/dx-react-grid-material-ui';

// const getChildRows = (row, rootRows) => (row ? row.items : rootRows);
const getChildRows = (row, rootRows) => {
    const childRows = rootRows.filter(
      (r) => r.parentId === (row ? row.id : null)
    );
    return childRows.length ? childRows : null;
  };

export default (props) => {
  const [columns] = useState([
    { name: 'id', title: 'ID' },
    { name: 'name', title: 'Name' },
    { name: 'level', title: 'Level' },
  ]);
  const [data] = useState(props.capabilities)
  const [tableColumnExtensions] = useState([
    { columnName: 'name', width: 300 },
  ]);

  console.log(props.capabilities)

  console.log(getChildRows)

  return (
    <Paper>
      <Grid
        rows={props.capabilities}
        columns={columns}
      >
        <TreeDataState />
        <CustomTreeData getChildRows={getChildRows}/>
        <Table columnExtensions={tableColumnExtensions}/>
        <TableHeaderRow />
        <TableTreeColumn for="id"/>
      </Grid>
    </Paper>
  );
};
