import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import CapabilityTableRow from "./CapabilityTableRow";
import TreeMenu from 'react-simple-tree-menu'
// import TreeViewMenu from 'react-simple-tree-menu'

export default class Capability extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            capabilities: []
        };
    }

    async componentDidMount() {
        const environmentResponse = await fetch(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`);
        const environmentData = await environmentResponse.json();
        this.setState({environmentId: environmentData.environmentId});

        const capabilityResponse = await fetch(`${process.env.REACT_APP_API_URL}/capability/getallbyenvironment/${this.state.environmentId}`);
        const capabilityData = await capabilityResponse.json();

        // capabilityData.map(i=>i.children=null);

        this.setState({capabilities: capabilityData});
        
        this.createDataTree(this.state.capabilities);
    }

    createDataTree(dataset) {
      const hashTable = Object.create(null);
      dataset.forEach(aData => hashTable[aData.ID] = {...aData, childNodes: []});
      const dataTree = [];
      dataset.forEach(aData => {
        if(aData.parentID) hashTable[aData.parentID].childNodes.push(hashTable[aData.ID])
        else dataTree.push(hashTable[aData.ID])
      });
      console.log(dataTree)
      return dataTree;
    };

    capabilityTable() {
        return this.state.capabilities.map((row, i) => {
            return <CapabilityTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        const environmentName = this.props.match.params.name;
        return(
            <div className="jumbotron">
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                        <li className="breadcrumb-item">Capabilities</li>
                    </ol>
                </nav>
                <h1 className='display-4'>Capabilities</h1>
                <br/><br/>
                <div className="row">
                    <div className="col-sm-9">
                    <TreeMenu
  cacheSearch
  data={[
    {
      key: 'mammal',
      label: 'Mammal',
      nodes: [
        {
          key: 'canidae',
          label: 'Canidae',
          nodes: [
            {
              key: 'dog',
              label: 'Dog',
              nodes: [],
              url: 'https://www.google.com/search?q=dog'
            },
            {
              key: 'fox',
              label: 'Fox',
              nodes: [],
              url: 'https://www.google.com/search?q=fox'
            },
            {
              key: 'wolf',
              label: 'Wolf',
              nodes: [],
              url: 'https://www.google.com/search?q=wolf'
            }
          ],
          url: 'https://www.google.com/search?q=canidae'
        }
      ],
      url: 'https://www.google.com/search?q=mammal'
    },
    {
      key: 'reptile',
      label: 'Reptile',
      nodes: [
        {
          key: 'squamata',
          label: 'Squamata',
          nodes: [
            {
              key: 'lizard',
              label: 'Lizard',
              url: 'https://www.google.com/search?q=lizard'
            },
            {
              key: 'snake',
              label: 'Snake',
              url: 'https://www.google.com/search?q=snake'
            },
            {
              key: 'gekko',
              label: 'Gekko',
              url: 'https://www.google.com/search?q=gekko'
            }
          ],
          url: 'https://www.google.com/search?q=squamata'
        }
      ],
      url: 'https://www.google.com/search?q=reptile'
    }
  ]}
  debounceTime={125}
  disableKeyboard={false}
  hasSearch
  onClickItem={function noRefCheck(){}}
  resetOpenNodesOnDataUpdate={false}
/>

                <table className='table table-striped'>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Parent Id</th>
                        <th>Level</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    { this.capabilityTable() }
                    </tbody>
                </table>
                    </div>
                    <div className="col-sm-2">

                    <div className="text-center">
                        <Link to={'edit'}>
                            <input type="button" value="Edit" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                        <br/>
                    <div className="text-center">
                        <Link to={'childcapability'}>
                            <input type="button" value="Child Capability" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                    </div>
                </div>
            </div>
        )
    }








}